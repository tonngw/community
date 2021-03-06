package com.nowcoder.community.controller;

import com.nowcoder.community.annotation.LoginRequired;
import com.nowcoder.community.entity.Comment;
import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.entity.Page;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.*;
import com.nowcoder.community.util.CommunityConstant;
import com.nowcoder.community.util.CommunityUtil;
import com.nowcoder.community.util.HostHolder;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author tonngw
 * @date 2022-01-15 12:54
 */
@Controller
@RequestMapping("/user")
public class UserController implements CommunityConstant {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Value("${community.path.domain}")
    private String domain;

    @Value("${community.path.upload}")
    private String uploadPath;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private LikeService likeService;

    @Autowired
    private FollowService followService;

    @Value("${qiniu.key.access}")
    private String accessKey;

    @Value("${qiniu.key.secret}")
    private String secretKey;

    @Value("${qiniu.bucket.header.name}")
    private String headerBucketName;

    @Value("${quniu.bucket.header.url}")
    private String headerBucketUrl;

    @Autowired
    private DiscussPostService discussPostService;
    
    @Autowired
    private CommentService commentService;

    // ??????????????????
    @RequestMapping(path = "/header/url", method = RequestMethod.POST)
    @ResponseBody
    public String updateHeaderUrl(String fileName) {
        if (StringUtils.isBlank(fileName)) {
            return CommunityUtil.getJSONString(1, "?????????????????????!");
        }

        String url = headerBucketUrl + "/" + fileName;
        userService.updateHeader(hostHolder.getUser().getId(), url);

        return CommunityUtil.getJSONString(0);
    }

    /**
     * ????????????????????????
     *
     * @return
     */
    @LoginRequired
    @RequestMapping(path = "/setting", method = RequestMethod.GET)
    public String getSettingPage(Model model) {
        // ??????????????????
        String fileName = CommunityUtil.generateUUID();
        // ??????????????????
        StringMap policy = new StringMap();
        policy.put("returnBody", CommunityUtil.getJSONString(0));
        // ??????????????????
        Auth auth = Auth.create(accessKey, secretKey);
        String uploadToken = auth.uploadToken(headerBucketName, fileName, 3600, policy);

        model.addAttribute("uploadToken", uploadToken);
        model.addAttribute("fileName", fileName);

        return "/site/setting";
    }

    // ?????? ????????????
    @LoginRequired
    @RequestMapping(path = "/upload", method = RequestMethod.POST)
    public String uploadHeader(MultipartFile headerImage, Model model) {
        if (headerImage == null) {
            model.addAttribute("error", "????????????????????????!");
            return "/site/setting";
        }

        String fileName = headerImage.getOriginalFilename();
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        if (StringUtils.isBlank(suffix)) {
            model.addAttribute("error", "????????????????????????!");
            return "/site/setting";
        }

        // ?????????????????????
        fileName = CommunityUtil.generateUUID() + suffix;
        // ???????????????????????????
        File dest = new File(uploadPath + "/" + fileName);
        try {
            // ????????????
            headerImage.transferTo(dest);
        } catch (IOException e) {
            logger.error("??????????????????: " + e.getMessage());
            throw new RuntimeException("??????????????????,?????????????????????!", e);
        }

        // ????????????????????????????????????(web????????????)
        // http://localhost:8080/community/user/header/xxx.png
        User user = hostHolder.getUser();
        String headerUrl = domain + contextPath + "/user/header/" + fileName;
        userService.updateHeader(user.getId(), headerUrl);

        return "redirect:/index";
    }

    // ?????? ????????????
    @RequestMapping(path = "/header/{fileName}", method = RequestMethod.GET)
    public void getHeader(@PathVariable("fileName") String fileName, HttpServletResponse response) {
        // ?????????????????????
        fileName = uploadPath + "/" + fileName;
        // ????????????
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        // ????????????
        response.setContentType("image/" + suffix);
        try (
                FileInputStream fis = new FileInputStream(fileName);
                OutputStream os = response.getOutputStream();
        ) {
            byte[] buffer = new byte[1024];
            int b = 0;
            while ((b = fis.read(buffer)) != -1) {
                os.write(buffer, 0, b);
            }
        } catch (IOException e) {
            logger.error("??????????????????: " + e.getMessage());
        }
    }

    /**
     * ????????????
     *
     * @param userId ?????? id
     * @param model  model
     * @return ??????????????????
     */
    @RequestMapping(path = "/profile/{userId}", method = RequestMethod.GET)
    public String getProfilePage(@PathVariable("userId") int userId, Model model) {
        User user = userService.findUserById(userId);
        if (user == null) {
            throw new RuntimeException("?????????????????????");
        }

        // ??????
        model.addAttribute("user", user);
        // ????????????
        int likeCount = likeService.findUserLikeCount(userId);
        model.addAttribute("likeCount", likeCount);

        // ????????????
        long followeeCount = followService.findFolloweeCount(userId, ENTITY_TYPE_USER);
        model.addAttribute("followeeCount", followeeCount);
        // ????????????
        long followerCount = followService.findFollowerCount(ENTITY_TYPE_USER, userId);
        model.addAttribute("followerCount", followerCount);
        // ???????????????
        boolean hasFollowed = false;
        if (hostHolder.getUser() != null) {
            hasFollowed = followService.hasFollowed(hostHolder.getUser().getId(), ENTITY_TYPE_USER, userId);
        }
        model.addAttribute("hasFollowed", hasFollowed);
        
        // ???????????????????????? Tab
        model.addAttribute("tabStatus", TAB_PROFILE);

        return "/site/profile";
    }

    @RequestMapping(path = "/update/password", method = RequestMethod.POST)
    public String updatePassword(String password, String newPassword,
                                 String reNewPassword, Model model) {
        // Map<String, Object> map = new HashMap<>();

        // ????????????????????????????????????
        if (!newPassword.equals(reNewPassword)) {
            model.addAttribute("newPasswordMsg", "????????????????????????????????????????????????");
            return "/site/setting";
        }

        // ?????? userId ????????????????????????
        User user = userService.findUserById(hostHolder.getUser().getId());
        if (user == null) {
            throw new RuntimeException("?????????????????????");
        }
        password = CommunityUtil.md5(password + user.getSalt());
        if (!user.getPassword().equals(password)) {
            model.addAttribute("passwordMsg", "?????????????????????????????????");
            return "/site/setting";
        }

        newPassword = CommunityUtil.md5(newPassword + user.getSalt());
        userService.updatePassword(hostHolder.getUser().getId(), newPassword);

        return "redirect:/logout";
    }

    /**
     * ??????????????????
     *
     * @param userId ?????? id
     * @param page ????????????
     * @param model model
     * @return ??????????????????
     */
    @RequestMapping(path = "/mypost/{userId}", method = RequestMethod.GET)
    public String getMyPostPage(@PathVariable("userId") int userId, Page page, Model model) {
        User user = userService.findUserById(userId);
        if (user == null) {
            throw new RuntimeException("?????????????????????");
        }
        model.addAttribute("user", user);
        
        // ??????????????????
        int rows = discussPostService.findDiscussPostRows(userId);
        page.setRows(rows);
        page.setPath("/user/mypost/" + userId);
        
        // ???????????????????????????
        model.addAttribute("rows", rows);

        // ?????????????????????????????????
        List<DiscussPost> list = discussPostService.findDiscussPost(userId,
                page.getOffset(), page.getLimit(), 0);
        List<Map<String, Object>> discussPosts = new ArrayList<>();
        if (list != null) {
            for (DiscussPost post : list) {
                Map<String, Object> map = new HashMap<>();
                map.put("post", post);

                // ????????????
                long likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_POST, post.getId());
                map.put("likeCount", likeCount);

                discussPosts.add(map);
            }
        }
        model.addAttribute("discussPosts", discussPosts);
        
        // ???????????????????????? Tab
        model.addAttribute("tabStatus", TAB_MYPOST);

        return "/site/my-post";
    }

    @RequestMapping(path = "/myreply/{userId}", method = RequestMethod.GET)
    public String getMyReplyPage(@PathVariable("userId") int userId, Page page, Model model) {
        User user = userService.findUserById(userId);
        if (user == null) {
            throw new RuntimeException("?????????????????????");
        }
        model.addAttribute("user", user);
        
        // ???????????????????????????????????????
        int rows = commentService.findAllCommentsCount(ENTITY_TYPE_POST, userId);

        // ??????????????????
        page.setRows(rows);
        page.setPath("/user/myreply/" + userId);
        // ?????????????????????????????????
        model.addAttribute("rows", rows);

        // ????????????????????????????????????????????????
        List<Comment> list = commentService.findAllComments(ENTITY_TYPE_POST, userId, page.getOffset(), page.getLimit());

        List<Map<String, Object>> comments = new ArrayList<>();
        if (list != null) {
            for (Comment comment : list) {
                // ???????????? id (entityId) ????????????
                DiscussPost post = discussPostService.findDiscussPostById(comment.getEntityId());
                Map<String, Object> map = new HashMap<>();
                map.put("post", post);
                map.put("comment", comment);
                
                comments.add(map);
            }
        }
        model.addAttribute("comments", comments);

        // ???????????????????????? Tab
        model.addAttribute("tabStatus", TAB_MYREPLY);

        return "/site/my-reply";
    }
}

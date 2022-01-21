package com.nowcoder.community.service;

import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.nowcoder.community.dao.DiscussPostMapper;
import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.util.SensitiveFilter;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author tonngw
 * @date 2022-01-14 13:06
 */
@Service
public class DiscussPostService {

    private static final Logger logger = LoggerFactory.getLogger(DiscussPostService.class);
    
    @Autowired
    private DiscussPostMapper discussPostMapper;
    
    @Autowired
    private SensitiveFilter sensitiveFilter;

    @Value("${caffeine.posts.max-size}")
    private int maxSize;

    @Value("${caffeine.posts.expire-seconds}")
    private int expireSeconds;

    // Caffeine核心接口: Cache, LoadingCache, AsyncLoadingCache
    
    // 帖子列表缓存：只缓存热门帖子（即 orderMode = 1）
    private LoadingCache<String, List<DiscussPost>> postListCache;

    // 帖子列表缓存
    private LoadingCache<Integer, Integer> postRowsCache;
    
    @PostConstruct
    public void init() {
        // 初始化帖子列表缓存
        postListCache = Caffeine.newBuilder()
                .maximumSize(maxSize)
                .expireAfterWrite(expireSeconds, TimeUnit.SECONDS)
                .build(new CacheLoader<String, List<DiscussPost>>() {
                    @Override
                    public @Nullable List<DiscussPost> load(@NonNull String key) throws Exception {
                        if (key == null || key.length() == 0) {
                            throw new IllegalArgumentException("参数错误！");
                        }
                        
                        String[] params = key.split(":");
                        if (params == null || params.length != 2) {
                            throw new IllegalArgumentException("参数错误！");
                        }
                        
                        int offset = Integer.valueOf(params[0]);
                        int limit = Integer.valueOf(params[1]);
                        
                        // 可以添加二级缓存 Redis
                        
                        // 最后如果缓存中没有数据再访问数据库
                        logger.debug("load post list from DB.");
                        return discussPostMapper.selectDiscussPosts(0, offset, limit, 1);
                    }
                });
        // 初始化帖子总数缓存
        postRowsCache = Caffeine.newBuilder()
                .maximumSize(maxSize)
                .expireAfterWrite(expireSeconds, TimeUnit.SECONDS)
                .build(new CacheLoader<Integer, Integer>() {
                    @Nullable
                    @Override
                    public Integer load(@NonNull Integer key) throws Exception {
                        logger.debug("load post rows from DB.");
                        return discussPostMapper.selectDiscussPostRows(key);
                    }
                });
    }
    
    public List<DiscussPost> findDiscussPost(int userId, int offset, int limit, int orderMode) {
        // 从本地缓存中获取热门帖子列表
        if (userId == 0 && orderMode == 1) {
            return postListCache.get(offset + ":" + limit);    
        }

        logger.debug("load post list from DB.");
        return discussPostMapper.selectDiscussPosts(userId, offset, limit, orderMode);
    }
    
    public int findDiscussPostRows(int userId) {
        // 从本地缓存中获取帖子总数，userId = 0 表示获取所有的帖子
        if (userId == 0) {
            return postRowsCache.get(userId);
        }
        
        return discussPostMapper.selectDiscussPostRows(userId);
    }
    
    public int addDiscussPost(DiscussPost post) {
        if (post == null) {
            throw new IllegalArgumentException("参数不能为空!");
        }
        
        // 转移 HTML 标记 防止恶意 html 标签注入，使用 Spring 自带的工具
        post.setTitle(HtmlUtils.htmlEscape(post.getTitle()));
        post.setContent(HtmlUtils.htmlEscape(post.getContent()));
        // 过滤敏感词
        post.setTitle(sensitiveFilter.filter(post.getTitle()));
        post.setContent(sensitiveFilter.filter(post.getContent()));
        
        return discussPostMapper.insertDiscussPost(post);
    }

    public DiscussPost findDiscussPostById(int id) {
        return discussPostMapper.selectDiscussPostById(id);
    }
    
    public int updateCommentCount(int id, int commentCount) {
        return discussPostMapper.updateCommentCount(id, commentCount);
    }
    
    public int updateType(int id, int type) {
        return discussPostMapper.updateType(id, type);
    }
    
    public int updateStatus(int id, int status) {
        return discussPostMapper.updateStatus(id, status);
    }

    public int updateScore(int id, double score) {
        return discussPostMapper.updateScore(id, score);
    }
}

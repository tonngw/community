package com.nowcoder.community.dao;

import com.nowcoder.community.entity.DiscussPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author tonngw
 * @date 2022-01-14 12:17
 */
@Mapper
public interface DiscussPostMapper {

    /**
     * 查询所有帖子，包含分页查询
     * @param userId 用户 id，id = 0 表示查询所有用户的数据
     * @param offset 开始查询位置
     * @param limit 查询条数
     * @return 帖子集合
     */
    List<DiscussPost> selectDiscussPosts(int userId, int offset, int limit);

    /**
     * 查询帖子条数（不包括拉黑的）
     * @Param 注解是用于给参数取别名的，如果参数列表中只有一个参数且要在 Mapper 配置文件
     * <if></if> 中使用，则必须使用别名
     * 
     * @param userId 用户 id，id = 0 表示查询所有用户的数据
     * @return 记录数
     */
    int selectDiscussPostRows(@Param("userId") int userId);

    /**
     * 发布帖子
     * @param discussPost 帖子
     * @return 影响行数
     */
    int insertDiscussPost(DiscussPost discussPost);

    /**
     * 根据 id 查询帖子
     * @param id 帖子 id
     * @return 帖子
     */
    DiscussPost selectDiscussPostById(int id);

    /**
     * 更新帖子评论数量
     * @param id 帖子 id
     * @param commentCount 评论数量
     * @return 影响行数
     */
    int updateCommentCount(int id, int commentCount);

    /**
     * 根据帖子 id 修改帖子类型（普通 置顶）
     * @param id 帖子 id
     * @param type 帖子类型
     * @return 影响行数
     */
    int updateType(int id, int type);

    /**
     * 根据帖子 id 修改帖子状态（正常 加精[精华] 删除[拉黑]）
     * @param id 帖子 id 
     * @param status 帖子状态
     * @return 影响行数
     */
    int updateStatus(int id, int status);
}

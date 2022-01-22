package com.nowcoder.community.dao;

import com.nowcoder.community.entity.Comment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author tonngw
 * @date 2022-01-15 21:38
 */
@Mapper
public interface CommentMapper {

    List<Comment> selectCommentsByEntity(int entityType, int entityId, int offset, int limit);

    int selectCountByEntity(int entityType, int entityId);

    int insertComment(Comment comment);

    Comment selectCommentById(int id);

    /**
     * 根据 entityType 和 userId 查询某个用户的帖子回复 / 评论回复总数
     *
     * @param entityType 实体类型
     * @param userId     用户 id
     * @return 数量
     */
    int selectAllCommentsCount(int entityType, int userId);

    /**
     * 根据 entityType 和 userId 查询某个用户所有的评论信息（其中每条信息中包含帖子 id） / 回复信息（其中每条信息中包含评论 id）
     *
     * @param userId 用户 id
     * @param entityType 实体类型
     * @param offset 起始偏移量
     * @param limit 显示条数
     * @return 评论信息
     */
    List<Comment> selectAllComments(int entityType, int userId, int offset, int limit);

}

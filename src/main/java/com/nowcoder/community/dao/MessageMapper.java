package com.nowcoder.community.dao;

import com.nowcoder.community.entity.Message;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author tonngw
 * @date 2022-01-16 10:14
 */
@Mapper
public interface MessageMapper {

    /**
     * 查询当前用户的会话列表，针对每个对话只返回一条最新的私信
     *
     * @param userId 用户 id
     * @param offset 分页起始位置
     * @param limit  显示条数
     * @return 会话列表
     */
    List<Message> selectConversations(int userId, int offset, int limit);

    /**
     * 查询当前用户的会话数量
     *
     * @param userId 用户 id
     * @return 会话数量
     */
    int selectConversationCount(int userId);

    /**
     * 查询某个会话所包含的私信列表
     *
     * @param conversationId 会话 id
     * @param offset         分页起始位置
     * @param limit          显示条数
     * @return 私信列表
     */
    List<Message> selectLetters(String conversationId, int offset, int limit);

    /**
     * 查询某个会话所包含的私信数量
     *
     * @param conversationId 会话 id
     * @return 私信数量
     */
    int selectLetterCount(String conversationId);

    /**
     * 查询未读私信的数量
     * 两种功能：
     * 1. 不传 conversationId 参数，查询总未读私信数量
     * 2.传 conversationId 参数，查询某个会话未读私信数量
     *
     * @param userId         用户 id
     * @param conversationId 会话 id
     * @return 未读私信数量
     */
    int selectLetterUnreadCount(int userId, String conversationId);

}

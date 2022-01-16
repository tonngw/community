package com.nowcoder.community.util;

/**
 * 生成 Redis Key 工具类
 * 
 * @author tonngw
 * @date 2022-01-16 17:31
 */
public class RedisKeyUtil {
    
    private static final String SPLIT = ":";
    private static final String PREFIX_ENTITY_LIKE = "like:entity";
    private static final String PREFIX_USER_LIKE = "like:user";
    // followee 被关注者
    private static final String PREFIX_FOLLOWEE = "followee";
    // follower 关注者
    private static final String PREFIX_FOLLOWER = "follower";
    
    // 某个实体的赞（帖子、评论）
    // like:entity:entityType:entityId -> set(userId)
    public static String getEntityLikeKey(int entityType, int entityId) {
        return PREFIX_ENTITY_LIKE + SPLIT + entityType + SPLIT + entityId;
    }

    // 某个用户的赞
    // like:user:userId -> int
    public static String getUserLikeKey(int userId) {
        return PREFIX_USER_LIKE + SPLIT + userId;
    }
    
    // 某个用户关注的实体
    // followee:userId:entityType -> zset(entityId,now)
    public static String getFolloweeKey(int userId, int entityType) {
        return PREFIX_FOLLOWEE + SPLIT + userId + SPLIT + entityType;
    }

    // 某个实体拥有的粉丝
    // follower:entityType:entityId -> zset(userId,now)
    public static String getFollowerKey(int entityType, int entityId) {
        return PREFIX_FOLLOWEE + SPLIT + entityType + SPLIT + entityId;
    }
}

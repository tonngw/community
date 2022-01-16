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
    
    // 某个实体的赞（帖子、评论）
    // like:entity:entityType:entityId -> set(userId)
    public static String getEntityLikeKey(int entityType, int entityId) {
        return PREFIX_ENTITY_LIKE + SPLIT + entityType + SPLIT + entityId;
    }
}

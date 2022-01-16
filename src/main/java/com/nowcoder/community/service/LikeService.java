package com.nowcoder.community.service;

import com.nowcoder.community.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

/**
 * @author tonngw
 * @date 2022-01-16 17:37
 */
@Service
public class LikeService {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 点赞
     *
     * @param userId     用户 id
     * @param entityType 实体类型
     * @param entityId   实体 id
     */
    public void like(int userId, int entityType, int entityId, int entityUserId) {
        /*String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
        // 判断当前用户是否在点赞集合里
        Boolean isMember = redisTemplate.opsForSet().isMember(entityLikeKey, userId);
        if (isMember) {
            redisTemplate.opsForSet().remove(entityLikeKey, userId);
        } else {
            redisTemplate.opsForSet().add(entityLikeKey, userId);
        }*/

        // 点赞功能重构，为了方便在个人主页中高效显示总点赞数，所以在 redis 中维护一个键值对 (用户 id, 总点赞数);
        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
                String userLikeKey = RedisKeyUtil.getUserLikeKey(entityUserId);

                // 在事务开始之前查询
                boolean isMember = operations.opsForSet().isMember(entityLikeKey, userId);

                // 开启 redis 事务
                operations.multi();

                if (isMember) {
                    operations.opsForSet().remove(entityLikeKey, userId);
                    operations.opsForValue().decrement(userLikeKey);
                } else {
                    operations.opsForSet().add(entityLikeKey, userId);
                    operations.opsForValue().increment(userLikeKey);
                }

                // 执行事务
                return operations.exec();
            }
        });
    }

    /**
     * 查询某实体点赞的数量
     *
     * @param entityType 实体类型
     * @param entityId   实体 id
     * @return 数量
     */
    public long findEntityLikeCount(int entityType, int entityId) {
        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
        return redisTemplate.opsForSet().size(entityLikeKey);
    }

    /**
     * 查询某人对某实体的点赞状态
     *
     * @param userId     用户 id
     * @param entityType 实体类型
     * @param entityId   实体 id
     * @return 点赞状态
     */
    public int findEntityLikeStatus(int userId, int entityType, int entityId) {
        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
        // 1 表示已赞 0 表示未赞
        return redisTemplate.opsForSet().isMember(entityLikeKey, userId) ? 1 : 0;
    }

    /**
     * 查询某个用户获得的赞
     *
     * @param userId 用户 id
     * @return 总赞数
     */
    public int findUserLikeCount(int userId) {
        String userLikeKey = RedisKeyUtil.getUserLikeKey(userId);
        Integer count = (Integer) redisTemplate.opsForValue().get(userLikeKey);
        return count == null ? 0 : count.intValue();
    }
}

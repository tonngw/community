package com.nowcoder.community.entity;

import java.util.HashMap;
import java.util.Map;

/**
 * 事件对象
 * 
 * @author tonngw
 * @date 2022-01-17 17:17
 */
public class Event {

    /**
     * 主题
     */
    private String topic;

    /**
     * 事件触发者 id
     */
    private int userId;

    /**
     * 实体类型
     */
    private int entityType;

    /**
     * 实体 id
     */
    private int entityId;

    /**
     * 事件接收者 id
     */
    private int entityUserId;

    /**
     * 用于封装额外数据
     */
    private Map<String, Object> data = new HashMap<>();

    public String getTopic() {
        return topic;
    }

    /**
     * 改造 set 方法，方便之后链式调用
     * @param topic
     * @return
     */
    public Event setTopic(String topic) {
        this.topic = topic;
        return this;
    }

    public int getUserId() {
        return userId;
    }

    public Event setUserId(int userId) {
        this.userId = userId;
        return this;
    }

    public int getEntityType() {
        return entityType;
    }

    public Event setEntityType(int entityType) {
        this.entityType = entityType;
        return this;
    }

    public int getEntityId() {
        return entityId;
    }

    public Event setEntityId(int entityId) {
        this.entityId = entityId;
        return this;
    }

    public int getEntityUserId() {
        return entityUserId;
    }

    public Event setEntityUserId(int entityUserId) {
        this.entityUserId = entityUserId;
        return this;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public Event setData(String key, Object value) {
        this.data.put(key, value);
        return this;
    }
}

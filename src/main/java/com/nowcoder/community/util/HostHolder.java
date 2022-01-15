package com.nowcoder.community.util;

import com.nowcoder.community.entity.User;
import org.springframework.stereotype.Component;

/**
 * 持有用户信息，用于代替 session 对象
 * 
 * @author tonngw
 * @date 2022-01-15 12:22
 */
@Component
public class HostHolder {
    private ThreadLocal<User> users = new ThreadLocal<User>();
    
    public void setUser(User user) {
        users.set(user);
    }
    
    public User getUser() {
        return users.get();
    }
    
    public void clear() {
        users.remove();
    }
}

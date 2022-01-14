package com.nowcoder.community.service;

import com.nowcoder.community.dao.UserMapper;
import com.nowcoder.community.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author tonngw
 * @date 2022-01-14 13:12
 */
@Service
public class UserService {
    
    @Autowired
    private UserMapper userMapper;
    
    public User findUserById(int userId) {
        return userMapper.selectById(userId);
    }
    
}

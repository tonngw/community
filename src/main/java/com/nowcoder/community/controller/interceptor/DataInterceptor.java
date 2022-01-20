package com.nowcoder.community.controller.interceptor;

import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.DataService;
import com.nowcoder.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 数据统计服务拦截器
 * 
 * @author tonngw
 * @date 2022-01-20 16:44
 */
@Component
public class DataInterceptor implements HandlerInterceptor {
    
    @Autowired
    private DataService dataService;
    
    @Autowired
    private HostHolder hostHolder;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 统计 UV
        String ip = request.getRemoteHost();
        dataService.recordUV(ip);
        
        // 统计 DAU
        User user = hostHolder.getUser();
        if (user != null) {
            dataService.recordDAU(user.getId());
        }
        
        return true;
    }
}

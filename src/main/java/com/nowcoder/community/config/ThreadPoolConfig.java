package com.nowcoder.community.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 线程池测试配置类
 * 
 * @author tonngw
 * @date 2022-01-20 17:59
 */
@Configuration
@EnableScheduling // 开启定时调度
@EnableAsync // 开启异步
public class ThreadPoolConfig {
}

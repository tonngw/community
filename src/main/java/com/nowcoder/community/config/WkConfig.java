package com.nowcoder.community.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.File;

/**
 * wkhtmltopdf 工具配置类：主要用于自动创建生成目录
 * 
 * @author tonngw
 * @date 2022-01-20 21:37
 */
@Configuration
public class WkConfig {

    private static final Logger logger = LoggerFactory.getLogger(WkConfig.class);

    @Value("${wk.image.storage}")
    private String wkImageStorage;
    
    // 初始化之后执行
    @PostConstruct
    public void init() {
        // 创建 WK 图片目录
        File file = new File(wkImageStorage);
        if (!file.exists()) {
            file.mkdir();
            logger.info("创建 WK 图片目录: " + wkImageStorage);
        }
    }
    
}

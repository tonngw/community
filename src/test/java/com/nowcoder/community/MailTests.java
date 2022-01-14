package com.nowcoder.community;

import com.nowcoder.community.util.MailClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;


/**
 * @author tonngw
 * @date 2022-01-14 18:11
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class MailTests {
    
    @Autowired
    private MailClient mailClient;

    /**
     * thymeleaf 模板引擎
     */
    @Autowired
    private TemplateEngine templateEngine;
    
    @Test
    public void testTextMail() {
        mailClient.sendMail("1336572985@qq.com", "TEST", "My First Java Mail!");
    }
    
    @Test
    public void testHtmlMail() {
        Context context = new Context();
        context.setVariable("username", "童果果");

        String content = templateEngine.process("/mail/demo", context);
        mailClient.sendMail("1336572985@qq.com", "HTML", content);
    }
    
}

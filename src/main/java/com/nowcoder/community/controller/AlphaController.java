package com.nowcoder.community.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 测试 Spring Boot
 * @author tonngw
 * @date 2022-01-14 11:38
 */

@Controller
@RequestMapping("/alpha")
public class AlphaController {
    
    @RequestMapping("/hello")
    @ResponseBody
    public String sayHello() {
        return "Hello Spring Boot.";
    }
    
}

package com.nowcoder.community.controller.advice;

import com.nowcoder.community.util.CommunityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Spring 统一异常处理
 *
 * @author tonngw
 * @date 2022-01-16 13:22
 */
@ControllerAdvice(annotations = Controller.class)
public class ExceptionAdvice {

    private static final Logger loggger = LoggerFactory.getLogger(ExceptionAdvice.class);

    /**
     * 统一异常处理注解 @ExceptionHandler
     *
     * @param e
     * @param request
     * @param response
     */
    @ExceptionHandler({Exception.class})
    public void handleException(Exception e, HttpServletRequest request, HttpServletResponse response) throws IOException {
        loggger.error("服务器发生异常：" + e.getMessage());
        // 记录错误栈消息
        for (StackTraceElement element : e.getStackTrace()) {
            loggger.error(element.toString());
        }

        String xRequestedWith = request.getHeader("x-requested-with");
        // 异步请求
        if ("XMLHttpRequest".equals(xRequestedWith)) {
            response.setContentType("application/plain;charset=utf-8");
            PrintWriter writer = response.getWriter();
            writer.write(CommunityUtil.getJSONString(1, "服务器异常"));
        } else {
            // 普通请求
            response.sendRedirect(request.getContextPath() + "/error");
        }
    }

}

package com.nowcoder.community;

import java.io.IOException;

/**
 * wkhtmltopdf 工具测试类
 * 包括 wkhtmltopdf、wkhtmltoimage
 * 
 * @author tonngw
 * @date 2022-01-20 21:33
 */

public class WkTests {

    public static void main(String[] args) {
        String cmd = "D:/Develop/Python Tools/wkhtmltopdf/bin/wkhtmltoimage --quality 75  https://www.nowcoder.com D:/Develop/data/wk-images/1.png";
        try {
            Runtime.getRuntime().exec(cmd);
            System.out.println("ok.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}

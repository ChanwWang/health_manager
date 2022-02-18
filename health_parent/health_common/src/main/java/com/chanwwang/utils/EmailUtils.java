package com.chanwwang.utils;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

import java.util.Map;

/**
 * @author ChanwWang
 * @version 1.0
 * 邮箱相关工具类
 */
public class EmailUtils {

    public static void initEmail(HtmlEmail email){
        email.setHostName("smtp.qq.com");//邮箱的SMTP服务器
        email.setCharset("utf-8");//设置发送的字符类型
        try {
            email.setFrom("659695190@qq.com","ChanwWang");
            //和发送人的邮箱和用户名
            email.setAuthentication("659695190@qq.com",
                    "ybrlysfztrgxbccg");
            //设置发送人到的邮箱和用户名和授权码(授权码是自己设置的)
//            email.addTo("aeteacher1234@gmail.com");
//
//            email.setSubject("测试");//设置发送主题
//            email.setMsg("你好我的朋友");//设置发送内容
        } catch (EmailException e) {
            e.printStackTrace();
        }
    }

    public static Integer sendValidateCodeEmail(Map<String,String> map) {
        HtmlEmail email = new HtmlEmail();
        initEmail(email);
        String receiver = map.get("receiver");

        return null;
    }



}

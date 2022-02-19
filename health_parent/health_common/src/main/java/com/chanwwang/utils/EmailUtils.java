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
    public static final String VALIDATE_CODE = "EMAIL_159620392";//发送邮箱验证码
    //模板样式: 您的验证码为${code},有效时间为5分钟
    public static final String ORDER_NOTICE = "EMAIL_159771588";//体检预约成功通知
    //模板样式:您已成功预约健康体检,体检时间为${code}

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

    public static void sendValidateCodeEmail(String templateCode,String emailAddress,String param) throws EmailException {
        // 设置超时时间-可自行调整
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");

        HtmlEmail email = new HtmlEmail();
        initEmail(email);

        email.addTo(emailAddress);
        String subject ="";
        if (VALIDATE_CODE.equals(templateCode)) {
            subject="[XX健康系统]用户验证码";
            email.setMsg("亲爱的用户,您的验证码为:" + param + ",5分钟内有效");
        }else if (ORDER_NOTICE.equals(templateCode)) {
            subject="[XX健康系统]您已预约成功";
            email.setMsg("亲爱的用户,您已成功预约在 " + param + " 的体检");

        }
        email.setSubject(subject);

        email.send();



    }



}

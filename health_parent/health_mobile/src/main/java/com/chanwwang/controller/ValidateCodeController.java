package com.chanwwang.controller;

import com.aliyuncs.exceptions.ClientException;
import com.chanwwang.constant.MessageConstant;
import com.chanwwang.constant.RedisMessageConstant;
import com.chanwwang.entity.Result;
import com.chanwwang.utils.EmailUtils;
import com.chanwwang.utils.SMSUtils;
import com.chanwwang.utils.ValidateCodeUtils;
import org.apache.commons.mail.EmailException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

/**
 * @author ChanwWang
 * @version 1.0
 * 验证码操作
 */
@RestController
@RequestMapping("/validateCode")
public class ValidateCodeController {
    @Autowired
    private JedisPool jedisPool;


    //用户在线体检预约发送邮箱验证码
    @RequestMapping("/send4Order")
    public Result send4Order(String email) {
        //随机生成4位数字验证码
        Integer validateCode = ValidateCodeUtils.generateValidateCode(4);

        try {
            //给用户发送验证码
            EmailUtils.sendValidateCodeEmail(EmailUtils.VALIDATE_CODE,email,validateCode.toString());
        } catch (EmailException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }


        //将验证码保存到redis(5分钟)
        jedisPool.getResource().setex(email + RedisMessageConstant.SENDTYPE_ORDER,60*5,
                validateCode.toString());
        return new Result(true,MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }

    //手机快速登录时发送手机验证码
    @RequestMapping("/send4Login")
    public Result send4Login(String telephone){
        Integer code = ValidateCodeUtils.generateValidateCode(6);//生成6位数字验证码
        try {
            //发送短信
//            SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE,telephone,code.toString());
        } catch (Exception e) {
            e.printStackTrace();
            //验证码发送失败
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
        System.out.println("发送的手机验证码为：" + code);
        //将生成的验证码缓存到redis
        jedisPool.getResource().setex(telephone+RedisMessageConstant.SENDTYPE_LOGIN,
                5 * 60,
                code.toString());
        //验证码发送成功
        return new Result(true,MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }
}

package com.chanwwang.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.chanwwang.constant.MessageConstant;
import com.chanwwang.constant.RedisConstant;
import com.chanwwang.constant.RedisMessageConstant;
import com.chanwwang.entity.Result;
import com.chanwwang.pojo.Order;
import com.chanwwang.service.OrderService;
import com.chanwwang.utils.EmailUtils;
import org.apache.commons.mail.EmailException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.util.Map;

/**
 * @author ChanwWang
 * @version 1.0
 * 体检预约处理
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private JedisPool jedisPool;
    @Reference
    private OrderService orderService;

    /**
     * 在线体检预约
     *
     * @param map email: "123@123.com"
     *            idCard: "123456789112345"
     *            name: "23"
     *            orderDate: "2022-02-20"
     *            setmealId: ""
     *            sex: "1"
     *            validateCode: "1234"
     * @return
     */
    @RequestMapping("/submit")
    public Result submit(@RequestBody Map<String, String> map) {


        //从Redis中获取保存的验证码
        String email = map.get("email");
        String validateCodeInRedis = jedisPool.getResource().get(email + RedisMessageConstant.SENDTYPE_ORDER);
        //将用户输入的验证码和Redis中保存的验证码进行比对
        String validateCode = map.get("validateCode");
        if (validateCodeInRedis == null ||
                !validateCodeInRedis.equals(validateCode)) {
            //比对不成功
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }
        Result result = null;
        //比对成功 ,调用服务
        try {
            map.put("orderType", Order.ORDERTYPE_WEIXIN);
            result = orderService.order(map);
        } catch (Exception e) {
            e.printStackTrace();
            return result;
        }

        if (result.isFlag()) {
            //预约成功,发送短信通知
            String orderDate = map.get("orderDate");

            new Thread(() -> {
                try {
                    EmailUtils.sendValidateCodeEmail(EmailUtils.ORDER_NOTICE, email, orderDate);
                } catch (EmailException e) {
                    e.printStackTrace();
                }
            }).start();
        }
        return result;
    }

    /**
     * 根据id查询预约信息，包括套餐信息和会员信息
     *
     * @param id
     * @return
     */
    @RequestMapping("/findById")
    public Result findById(Integer id) {
        Map map;
        try {
            map = orderService.findById(id);
            //查询预约信息成功
        } catch (Exception e) {
            e.printStackTrace();
            //查询预约信息失败
            return new Result(false, MessageConstant.QUERY_ORDER_FAIL);
        }
        return new Result(true, MessageConstant.QUERY_ORDER_SUCCESS, map);


    }
}



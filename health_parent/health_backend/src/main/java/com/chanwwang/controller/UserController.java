package com.chanwwang.controller;

import com.chanwwang.constant.MessageConstant;
import com.chanwwang.entity.Result;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ChanwWang
 * @version 1.0
 * 用户操作
 */
@RestController
@RequestMapping("/user")
public class UserController {

    //获得当前登录用户的用户名
    @RequestMapping("/getUsername")
    public Result getUsername() {
        //当Spring security完成认证后,会将当前用户信息保存到框架提供的上下文对象
        //通过上下文对象 获得 认证信息对象
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User user = (User) authentication.getPrincipal();
        System.out.println(user);
        if (user == null) {
            return new Result(false, MessageConstant.GET_USERNAME_FAIL);
        }
        String username = user.getUsername();

        return new Result(true, MessageConstant.GET_USERNAME_SUCCESS,username);
    }
}

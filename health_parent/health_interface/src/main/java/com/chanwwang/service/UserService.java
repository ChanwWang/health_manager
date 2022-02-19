package com.chanwwang.service;

import com.chanwwang.pojo.User;

/**
 * @author ChanwWang
 * @version 1.0
 */
public interface UserService {

    public User findByUsername(String username);

}

package com.chanwwang.dao;

import com.chanwwang.pojo.User;

/**
 * @author ChanwWang
 * @version 1.0
 */
public interface UserDao {

    public User findByUsername(String username);
}

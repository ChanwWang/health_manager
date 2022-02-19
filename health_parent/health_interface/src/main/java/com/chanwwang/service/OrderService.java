package com.chanwwang.service;

import com.chanwwang.entity.Result;

import java.util.Map;

/**
 * @author ChanwWang
 * @version 1.0
 */
public interface OrderService {
    public Result order(Map<String,String> map) throws Exception;

    //根据id查询预约信息，包括体检人信息、套餐信息
    public Map findById(Integer id) throws Exception;
}

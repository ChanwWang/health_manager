package com.chanwwang.service;

import com.chanwwang.pojo.OrderSetting;

import java.util.List;
import java.util.Map;

/**
 * @author ChanwWang
 * @version 1.0
 */
public interface OrderSettingService {

    public void add(List<OrderSetting> data);
    public List<Map> getOrderSettingByMonth(String date);//参数格式为：2019-03

    public void editNumberByDate(OrderSetting orderSetting);
}

package com.chanwwang.dao;

import com.chanwwang.pojo.OrderSetting;
import org.apache.poi.ss.formula.functions.Count;

import java.util.Date;

/**
 * @author ChanwWang
 * @version 1.0
 */
public interface OrderSettingDao {

    public void add(OrderSetting orderSetting);

    public void editNumberByOrderDate(OrderSetting orderSetting);

    public long findCountByOrderDate(Date orderDate);
}

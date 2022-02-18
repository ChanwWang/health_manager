package com.chanwwang.dao;

import com.chanwwang.pojo.OrderSetting;
import org.apache.poi.ss.formula.functions.Count;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author ChanwWang
 * @version 1.0
 */
public interface OrderSettingDao {

    public void add(OrderSetting orderSetting);

    public void editNumberByOrderDate(OrderSetting orderSetting);

    public long findCountByOrderDate(Date orderDate);

    public List<OrderSetting> selectOrderSettingByMonth(Map map);
}

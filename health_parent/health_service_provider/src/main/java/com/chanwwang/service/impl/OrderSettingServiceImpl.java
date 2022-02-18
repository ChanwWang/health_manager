package com.chanwwang.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.chanwwang.dao.OrderSettingDao;
import com.chanwwang.pojo.OrderSetting;
import com.chanwwang.service.OrderSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author ChanwWang
 * @version 1.0
 * 预约设置服务类
 */
@Service(interfaceClass = OrderSettingService.class)
@Transactional
public class OrderSettingServiceImpl implements OrderSettingService {

    @Autowired
    private OrderSettingDao orderSettingDao;

    //批量导入预约设置数据
    public void add(List<OrderSetting> data) {
        //判断当前日期是否已经有数据
        if (data != null && data.size() > 0) {
            for (OrderSetting orderSetting : data) {
                //判断当前日期是否已经进行了预约设置
                long countByOrderDate =
                        orderSettingDao.findCountByOrderDate(orderSetting.getOrderDate());
                if (countByOrderDate > 0) {
                    //已经进行了预约设置,执行更新操作
                    orderSettingDao.editNumberByOrderDate(orderSetting);
                } else {
                    //没有进行预约设置,执行插入操作
                    orderSettingDao.add(orderSetting);
                }
            }
        }
    }

    //根据日期查询预约设置数据
    public List<Map> getOrderSettingByMonth(String date) {//2022-3
        String begin = date + "-1";//2022-3-1
        String end = date + "-31";//2022-3-31

        Map<String, String> map = new HashMap<>();
        map.put("begin", begin);
        map.put("end", end);
        //调用DAO,根据日期范围查询预约设置数据
        List<OrderSetting> orderSettings = orderSettingDao.selectOrderSettingByMonth(map);
        List<Map> results = new ArrayList<>();

        if (orderSettings != null && orderSettings.size() > 0) {
            for (OrderSetting orderSetting : orderSettings) {
                Map<String, Object> orderSettingMap = new HashMap<>();
                //获取日期数字(几号)
                orderSettingMap.put("date", orderSetting.getOrderDate().getDate());
                //获取预约人数
                orderSettingMap.put("number", orderSetting.getNumber());
                //获取已预约人数
                orderSettingMap.put("reservations", orderSetting.getReservations());
                results.add(orderSettingMap);
            }
        }
        return results;
    }


    //根据指定日期修改可预约人数
    public void editNumberByDate(OrderSetting orderSetting) {
        Date orderDate = orderSetting.getOrderDate();
        //根据日期查询是否已经进行了预约设置
        if (orderSettingDao.findCountByOrderDate(orderDate) > 0) {
            //已经设置 执行更新操作
            orderSettingDao.editNumberByOrderDate(orderSetting);
        } else {
            //没有设置 执行插入操作
            orderSettingDao.add(orderSetting);
        }
    }


}

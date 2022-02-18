package com.chanwwang.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.chanwwang.dao.OrderSettingDao;
import com.chanwwang.pojo.OrderSetting;
import com.chanwwang.service.OrderSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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


}

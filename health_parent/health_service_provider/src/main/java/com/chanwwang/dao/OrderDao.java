package com.chanwwang.dao;

import com.chanwwang.pojo.Order;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Map;


public interface OrderDao {
    public void add(Order order);
    public List<Order> findByCondition(Order order);


    public Map findById4Detail(@Param("id") Integer id);

    public Integer findOrderCountByDate(String date);
    public Integer findOrderCountAfterDate(String date);
    public Integer findVisitsCountByDate(String date);
    public Integer findVisitsCountAfterDate(String date);

    public List<Map> findHotSetmeal();
}

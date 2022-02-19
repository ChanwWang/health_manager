package com.chanwwang.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.chanwwang.constant.MessageConstant;
import com.chanwwang.dao.MemberDao;
import com.chanwwang.dao.OrderDao;
import com.chanwwang.dao.OrderSettingDao;
import com.chanwwang.entity.Result;
import com.chanwwang.pojo.Member;
import com.chanwwang.pojo.Order;
import com.chanwwang.pojo.OrderSetting;
import com.chanwwang.service.OrderService;
import com.chanwwang.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author ChanwWang
 * @version 1.0
 * 体检预约设置
 */
@Service(interfaceClass = OrderService.class)
@Transactional
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderSettingDao orderSettingDao;
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private OrderDao orderDao;


    @Override
    public Result order(Map<String, String> map) throws Exception {

        //体检预约方法处理逻辑比较复杂，需要进行如下业务处理：
        //1、检查用户所选择的预约日期是否已经提前进行了预约设置，如果没有设置则无法进行预约
        String orderDate = map.get("orderDate");
        Date date = DateUtils.parseString2Date(orderDate);
        OrderSetting orderSetting = orderSettingDao.
                selectByOrderDate(date);
        if (orderSetting == null) {
            //指定日期没有进行预约设置,无法完成体检预约
            return new Result(false, MessageConstant.SELECTED_DATE_CANNOT_ORDER);
        }

        //2、检查用户所选择的预约日期是否已经约满，如果已经约满则无法预约
        int number = orderSetting.getNumber();//可预约人数
        int reservations = orderSetting.getReservations();//已预约人数
        if (reservations >= number) {
            //已经约满 不能预约
            return new Result(false, MessageConstant.ORDER_FULL);
        }

        //3、检查用户是否重复预约（同一个用户在同一天预约了同一个套餐），如果是重复预约则无法完成再次预约
        String email = map.get("email");
        Member member = memberDao.findByEmail(email);
        if (member != null) {
            //判断是否在重复预约
            Integer memberId = member.getId();//会员ID
            String setmealId = map.get("setmealId");//套餐ID

            Order order = new Order(memberId, date, Integer.parseInt(setmealId));
            //根据条件查询
            List<Order> list = orderDao.findByCondition(order);
            if (list != null && list.size() > 0) {
                //说明用户在重复预约,无法再次预约
                return new Result(false, MessageConstant.HAS_ORDERED);
            }

        } else {
            //4、检查当前用户是否为会员，如果是会员则直接完成预约，如果不是会员则自动完成注册并进行预约
            member = new Member();
            member.setName((String) map.get("name"));
            member.setIdCard((String) map.get("idCard"));
            member.setSex((String) map.get("sex"));
            member.setRegTime(new Date());
            member.setEmail(email);
            memberDao.add(member);//自动完成注册
        }

        //5、预约成功，更新当日的已预约人数
        Order order = new Order(member.getId(),//会员id
                date,//预约日期
                map.get("orderType"),//预约类型
                Order.ORDERSTATUS_NO,//到诊状态
                Integer.parseInt(map.get("setmealId")));//套餐Id
        orderDao.add(order);

        orderSetting.setReservations(orderSetting.getReservations() + 1);//设置已预约人数+1
        orderSettingDao.editReservationsByOrderDate(orderSetting);
        return new Result(true, MessageConstant.ORDER_SUCCESS, order.getId());
    }

    //根据预约ID查询 预约相关信息(体检人姓名,预约日期,套餐名称,预约类型)
    public Map findById(Integer id) throws Exception {
        Map map = orderDao.findById4Detail(id);
        if(map != null){
            //处理日期格式
            Date orderDate = (Date) map.get("orderDate");
            map.put("orderDate",DateUtils.parseDate2String(orderDate));
        }
        return map;
    }
}

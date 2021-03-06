package com.chanwwang.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.chanwwang.constant.MessageConstant;
import com.chanwwang.entity.Result;
import com.chanwwang.pojo.OrderSetting;
import com.chanwwang.service.OrderSettingService;
import com.chanwwang.utils.POIUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author ChanwWang
 * @version 1.0
 */
@RestController
@RequestMapping("/ordersetting")
public class OrderSettingController {

    @Reference
    private OrderSettingService orderSettingService;

    //文件上传,实现预约设置数据批量导入
    @RequestMapping("/upload")
    public Result upload(@RequestParam("excelFile") MultipartFile excelFile) {

        try {
            //使用POI解析表格数据
            List<String[]> list = POIUtils.readExcel(excelFile);
            //将解析得到的表格数据封装到新的集合中
            List<OrderSetting> data = new ArrayList<>();

            for (String[] strings : list) {
                String orderDate = strings[0];
                String number = strings[1];
                OrderSetting orderSetting = new OrderSetting(new Date(orderDate), Integer.parseInt(number));
                data.add(orderSetting);
            }
            //通过dubbo远程调用服务实现数据批量导入到数据库
            orderSettingService.add(data);
        } catch (IOException e) {
            e.printStackTrace();
            //文件解析失败
            return new Result(false, MessageConstant.IMPORT_ORDERSETTING_FAIL);
        }

        return new Result(true, MessageConstant.IMPORT_ORDERSETTING_SUCCESS);
    }

    //根据月份查询对应的预约设置数据
    @RequestMapping("/getOrderSettingByMonth")
    public Result getOrderSettingByMonth(String date) {//date格式为:yyyy-MM
        //基于页面要的格式 date: 1, number: 120, reservations: 1 封装成Map集合的List集合
        List<Map> map;

        try {
            map = orderSettingService.getOrderSettingByMonth(date);
        } catch (Exception e) {
            e.printStackTrace();
            return  new Result(false,MessageConstant.GET_ORDERSETTING_FAIL);
        }
        return  new Result(true,MessageConstant.GET_ORDERSETTING_SUCCESS,map);
    }

    //根据月份查询对应的预约设置数据
    @RequestMapping("/editNumberByDate")
    public Result editNumberByDate(@RequestBody OrderSetting orderSetting) {//date格式为:yyyy-MM
        //基于页面要的格式 date: 1, number: 120, reservations: 1 封装成Map集合的List集合


        try {
            orderSettingService.editNumberByDate(orderSetting);
        } catch (Exception e) {
            e.printStackTrace();
            return  new Result(false,MessageConstant.ORDERSETTING_FAIL);
        }
        return  new Result(true,MessageConstant.ORDERSETTING_SUCCESS);
    }
}

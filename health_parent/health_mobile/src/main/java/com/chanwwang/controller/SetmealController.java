package com.chanwwang.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.chanwwang.constant.MessageConstant;
import com.chanwwang.entity.Result;
import com.chanwwang.pojo.Setmeal;
import com.chanwwang.service.SetmealService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author ChanwWang
 * @version 1.0
 * 套餐管理
 */

@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    @Reference
    private SetmealService setmealService;

    //查询所有套餐
    @RequestMapping("/getAllSetmeal")
    public Result getAllSetmeal() {

        List<Setmeal> setmeals;

        try {
            setmeals = setmealService.findAll();
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_SETMEAL_FAIL);
        }
        return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS,setmeals);

    }

    //根据id查询套餐信息
    @RequestMapping("/findById")
    public Result findById(Integer id) {
        Setmeal setmeal;
        try {
            setmeal = setmealService.findById4Detail(id);

            if (setmeal == null) {
                throw new RuntimeException();
            }

        } catch (Exception e) {
            e.printStackTrace();
            //服务调用失败
            return new Result(false, MessageConstant.QUERY_SETMEAL_FAIL);
        }
        return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS,setmeal);
    }
}

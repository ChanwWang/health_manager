package com.chanwwang.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.chanwwang.constant.MessageConstant;
import com.chanwwang.entity.Result;
import com.chanwwang.service.MemberService;
import com.chanwwang.service.SetmealService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author ChanwWang
 * @version 1.0
 * 处理报表
 */
@RestController
@RequestMapping("/report")
public class ReportController {
    @Reference
    private SetmealService setmealService;
    @Reference
    private MemberService memberService;

    //会员数量折线图数据
    @RequestMapping("/getMemberReport")
    public Result getMemberReport() {
        HashMap<String, Object> map = new HashMap<>();
        ArrayList<String> months = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();//获得当前时间日历对象
        calendar.add(Calendar.MONTH, -12);//当前时间往前推12个月的时间

        //计算过去一年的12个月

        for (int i = 0; i < 12; i++) {
            calendar.add(Calendar.MONTH, 1);//获得时间往后推1个月
            Date date = calendar.getTime();
            months.add(new SimpleDateFormat("yyyy.MM").format(date));
        }
        map.put("months", months);

        List<Integer> memberCount = null;
        try {
            memberCount = memberService.findMemberCountByMonths(months);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_MEMBER_NUMBER_REPORT_FAIL);
        }
        map.put("memberCount", memberCount);
        return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS,map);

    }

    //套餐预约占比饼形图
    @RequestMapping("/getSetmealReport")
    public Result getSetmealReport() {
        Map<String, Object> map = new HashMap<>();
        List<String> setmealNames = new ArrayList<>();
        List<Map<String,Object>> setmealCount;

        try {
            setmealCount = setmealService.findSetmealCount();
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_SETMEAL_COUNT_REPORT_FAIL);
        }
        for (Map<String, Object> nameValueMap : setmealCount) {
            String name = (String) nameValueMap.get("name");
            setmealNames.add(name);
        }
        map.put("setmealNames",setmealNames);
        map.put("setmealCount",setmealCount);

        return new Result(true, MessageConstant.GET_SETMEAL_COUNT_REPORT_SUCCESS,map);

    }
}

package com.chanwwang.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.chanwwang.constant.MessageConstant;
import com.chanwwang.entity.PageResult;
import com.chanwwang.entity.QueryPageBean;
import com.chanwwang.entity.Result;
import com.chanwwang.pojo.CheckItem;
import com.chanwwang.service.CheckItemService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author ChanwWang
 * @version 1.0
 * 检查项管理
 */
@RestController
@RequestMapping(("/checkitem"))
public class CheckItemController {

    @Reference//查找服务
    private CheckItemService checkItemService;

    //新增检查项
//    @PostMapping
    @RequestMapping("/add")
    public Result add(@RequestBody CheckItem checkItem) {

        try {
            checkItemService.add(checkItem);
        } catch (Exception e) {
            e.printStackTrace();
            //服务调用失败
            return new Result(false, MessageConstant.ADD_CHECKITEM_FAIL);
        }
        return new Result(true, MessageConstant.ADD_CHECKITEM_SUCCESS);
    }

    //分页查找检查项
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
        PageResult pageResult = checkItemService.pageQuery(queryPageBean);


        return pageResult;
    }

    //删除检查项
    @RequestMapping("/delete")
    public Result delete(Integer id) {

        try {
            checkItemService.deleteById(id);
        } catch (Exception e) {
            e.printStackTrace();
            //服务调用失败
            return new Result(false, MessageConstant.DELETE_CHECKITEM_FAIL);
        }
        return new Result(true, MessageConstant.DELETE_CHECKITEM_SUCCESS);
    }

    //编辑检查项
    @RequestMapping("/update")
    public Result update(@RequestBody CheckItem checkItem) {

        try {
            boolean flag = checkItemService.update(checkItem);
            if (!flag) {
                throw  new RuntimeException();
            }
        } catch (Exception e) {
            e.printStackTrace();
            //服务调用失败
            return new Result(false, MessageConstant.EDIT_CHECKITEM_FAIL);
        }
        return new Result(true, MessageConstant.EDIT_CHECKITEM_SUCCESS);
    }

    //查询检查项 通过id
    @RequestMapping("/findById")
    public Result findById(Integer id) {
        CheckItem checkItem;
        try {
             checkItem = checkItemService.findById(id);

        } catch (Exception e) {
            e.printStackTrace();
            //服务调用失败
            return new Result(false, MessageConstant.QUERY_CHECKITEM_FAIL);
        }
        return new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS,checkItem);
    }

    //查询所有检查项
    @RequestMapping("/findAll")
    public Result findAll() {
        List<CheckItem> checkItems;
        try {
            checkItems = checkItemService.findAll();

        } catch (Exception e) {
            e.printStackTrace();
            //服务调用失败
            return new Result(false, MessageConstant.QUERY_CHECKITEM_FAIL);
        }
        return new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS,checkItems);
    }

}

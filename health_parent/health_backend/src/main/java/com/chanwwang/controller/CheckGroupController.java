package com.chanwwang.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.chanwwang.constant.MessageConstant;
import com.chanwwang.entity.PageResult;
import com.chanwwang.entity.QueryPageBean;
import com.chanwwang.entity.Result;
import com.chanwwang.pojo.CheckGroup;
import com.chanwwang.pojo.CheckItem;
import com.chanwwang.service.CheckGroupService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author ChanwWang
 * @version 1.0
 */
@RestController
@RequestMapping(("/checkgroup"))
public class CheckGroupController {
    @Reference
    private CheckGroupService checkGroupService;

    //新增检查组
    @RequestMapping("/add")
    public Result add(@RequestBody CheckGroup checkGroup,Integer[] checkitemIds){

        try {
            checkGroupService.add(checkGroup,checkitemIds);
        } catch (Exception e) {
            e.printStackTrace();
            //服务调用失败
            return new Result(false, MessageConstant.ADD_CHECKGROUP_FAIL);
        }
        return new Result(true, MessageConstant.ADD_CHECKGROUP_SUCCESS);

    }
    //更新检查组信息,并将关联信息添加到关联表
    @RequestMapping("/update")
    public Result Update(@RequestBody CheckGroup checkGroup,Integer[] checkitemIds) {
        try {
            boolean flag = checkGroupService.update(checkGroup,checkitemIds);
            if (!flag) {
                throw  new RuntimeException();
            }
        } catch (Exception e) {
            e.printStackTrace();
            //服务调用失败
            return new Result(false, MessageConstant.EDIT_CHECKGROUP_FAIL);
        }
        return new Result(true, MessageConstant.EDIT_CHECKGROUP_SUCCESS);
    }

    //分页查找检查项
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
        PageResult pageResult = checkGroupService.pageQuery(queryPageBean);
        return pageResult;
    }

    //查询检查项 通过id
    @RequestMapping("/findById")
    public Result findById(Integer id) {
        CheckGroup checkGroup;
        try {
            checkGroup = checkGroupService.findById(id);
            if (checkGroup == null) {
                throw new RuntimeException();
            }

        } catch (Exception e) {
            e.printStackTrace();
            //服务调用失败
            return new Result(false, MessageConstant.QUERY_CHECKGROUP_FAIL);
        }
        return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS,checkGroup);
    }

    @RequestMapping("/findCheckItemIdsByCheckGroupId")
    public Result findCheckItemIdsByCheckGroupId(Integer id) {
        List<Integer> checkItemIds;

        try {
            checkItemIds= checkGroupService.findCheckItemIdsByCheckGroupId(id);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_CHECKITEM_FAIL);
        }

        return new Result(true,MessageConstant.QUERY_CHECKITEM_SUCCESS,checkItemIds);
    }



    //删除检查组信息,
    @RequestMapping("/delete")
    public Result delete(Integer id) {
        try {
            checkGroupService.deleteById(id);

        } catch (Exception e) {
            e.printStackTrace();
            //服务调用失败
            return new Result(false, MessageConstant.DELETE_CHECKGROUP_FAIL);
        }
        return new Result(true, MessageConstant.DELETE_CHECKGROUP_SUCCESS);
    }

    //查询所有检查组
    @RequestMapping("/findAll")
    public Result findAll() {
        List<CheckGroup> checkGroups;
        try {
            checkGroups = checkGroupService.findAll();

        } catch (Exception e) {
            e.printStackTrace();
            //服务调用失败
            return new Result(false, MessageConstant.QUERY_CHECKGROUP_FAIL);
        }
        return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS,checkGroups);
    }
}

package com.chanwwang.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.chanwwang.constant.MessageConstant;
import com.chanwwang.constant.RedisConstant;
import com.chanwwang.entity.PageResult;
import com.chanwwang.entity.QueryPageBean;
import com.chanwwang.entity.Result;
import com.chanwwang.pojo.CheckGroup;
import com.chanwwang.pojo.Setmeal;
import com.chanwwang.service.SetmealService;
import com.chanwwang.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * @author ChanwWang
 * @version 1.0
 * 体检套餐管理
 */
@RestController
@RequestMapping("/setmeal")
public class SetMealController {
    //使用JedisPool操作Redis服务
    @Autowired
    private JedisPool jedisPool;

    @Reference
    private SetmealService setmealService;

    //新增套餐
    @RequestMapping("/add")
    public Result add(@RequestBody Setmeal setmeal, Integer[] checkgroupIds){

        try {
            setmealService.add(setmeal,checkgroupIds);
        } catch (Exception e) {
            e.printStackTrace();
            //服务调用失败
            return new Result(false, MessageConstant.ADD_SETMEAL_FAIL);
        }
        return new Result(true, MessageConstant.ADD_SETMEAL_SUCCESS);

    }

    //更新套餐信息,并将关联信息添加到关联表
    @RequestMapping("/update")
    public Result Update(@RequestBody Setmeal setmeal,Integer[] checkgroupIds) {
        try {
            boolean flag = setmealService.update(setmeal,checkgroupIds);
            if (!flag) {
                throw  new RuntimeException();
            }
        } catch (Exception e) {
            e.printStackTrace();
            //服务调用失败
            return new Result(false, MessageConstant.EDIT_SETMEAL_FAIL);
        }
        return new Result(true, MessageConstant.EDIT_SETMEAL_SUCCESS);
    }

    //删除套餐信息,
    @RequestMapping("/delete")
    public Result delete(Integer id) {
        try {
            setmealService.deleteById(id);

        } catch (Exception e) {
            e.printStackTrace();
            //服务调用失败
            return new Result(false, MessageConstant.DELETE_SETMEAL_FAIL);
        }
        return new Result(true, MessageConstant.DELETE_SETMEAL_SUCCESS);
    }

    @RequestMapping("/findCheckGroupIdsBySetmealId")
    public Result findCheckGroupIdsBySetmealId(Integer id) {
        List<Integer> checkgroupIds;

        try {
            checkgroupIds= setmealService.findCheckGroupIdsBySetmealId(id);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_CHECKGROUP_FAIL);
        }

        return new Result(true,MessageConstant.QUERY_CHECKGROUP_SUCCESS,checkgroupIds);
    }

    //查询所有检查组
    @RequestMapping("/findAll")
    public Result findAll() {
        List<Setmeal> setmeals;
        try {
            setmeals = setmealService.findAll();

        } catch (Exception e) {
            e.printStackTrace();
            //服务调用失败
            return new Result(false, MessageConstant.QUERY_SETMEAL_FAIL);
        }
        return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS,setmeals);
    }

    //查询套餐 通过id
    @RequestMapping("/findById")
    public Result findById(Integer id) {
        Setmeal setmeal;
        try {
            setmeal = setmealService.findById(id);
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

    //分页查询
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){

        PageResult pageResult = setmealService.pageQuery(queryPageBean);
        return pageResult;
    }



    //文件上传功能
    @RequestMapping("/upload")
    public Result upload(@RequestParam("imgFile") MultipartFile imgFile) {

        //获得上传文件的原始文件名
        String originalFilename = imgFile.getOriginalFilename();
        //截取原始文件后缀名 .jpg
        String extension =
                originalFilename.substring(originalFilename.lastIndexOf(".") - 1);

        String fileName = UUID.randomUUID().toString() + extension;
        try {
            //将文件上传到七牛云服务器
            QiniuUtils.upload2Qiniu(imgFile.getBytes(),fileName);
            //将上传图片名称存入Redis，基于Redis的Set集合存储
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_RESOURCES,fileName);
        } catch (IOException e) {
            e.printStackTrace();
            //图片上传失败
            return new Result(false, MessageConstant.PIC_UPLOAD_FAIL);
        }
        return new Result(true,MessageConstant.PIC_UPLOAD_SUCCESS,fileName);
    }

}

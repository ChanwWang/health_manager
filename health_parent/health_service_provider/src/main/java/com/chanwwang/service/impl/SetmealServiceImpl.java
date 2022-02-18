package com.chanwwang.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.chanwwang.constant.RedisConstant;
import com.chanwwang.dao.SetmealDao;
import com.chanwwang.entity.PageResult;
import com.chanwwang.entity.QueryPageBean;
import com.chanwwang.pojo.CheckGroup;
import com.chanwwang.pojo.Setmeal;
import com.chanwwang.service.SetmealService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ChanwWang
 * @version 1.0
 */
@Service(interfaceClass = SetmealService.class)
@Transactional
public class SetmealServiceImpl implements SetmealService {
    //使用JedisPool操作Redis服务
    @Autowired
    private JedisPool jedisPool;
    @Autowired
    private SetmealDao setmealDao;

    @Override
    //检查组分页查询
    public PageResult pageQuery(QueryPageBean queryPageBean) {
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        String queryString = queryPageBean.getQueryString();

        //完成分页查询,基于mybatis框架提供的分页助手插件完成
        PageHelper.startPage(currentPage, pageSize);
        Page<Setmeal> page = setmealDao.selectByCondition(queryString);

        long total = page.getTotal();
        List<Setmeal> rows = page.getResult();

        return new PageResult(total, rows);
    }

    @Override
    //新增套餐信息,同时需要关联检查组
    public void add(Setmeal setmeal, Integer[] checkGroupIds) {
        setmealDao.add(setmeal);
        Integer setmealId = setmeal.getId();

        this.addSetmealAndCheckGroup(setmealId, checkGroupIds);
        //将图片名称保存到Redis集合中

        String fileName = setmeal.getImg();
        jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES, fileName);

    }
    //    更新套餐数据
    @Override
    public boolean update(Setmeal setmeal, Integer[] checkGroupIds) {
        //修改检查组基本信息,操作检查组t_checkgroup表
        //是否修改成功
        boolean flag = setmealDao.update(setmeal) > 0;
        Integer setmealId = setmeal.getId();


        //将图片名称保存到Redis集合中
        String fileName = setmeal.getImg();
        jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES, fileName);

        //清理当前检查组关联的检查项,操作中间关系表 t_checkgroup_checkitem表
        setmealDao.deleteAssociation(setmealId);
        //重新建立当前检查组和检查项的关联关系

        addSetmealAndCheckGroup(setmealId, checkGroupIds);
        return flag;
    }


    //根据套餐id删除
    @Override
    public void deleteById(Integer id) {
        Setmeal setmeal = findById(id);
        String fileName = setmeal.getImg();
        boolean flag = setmealDao.deleteById(id)>0;
        if ( !flag ) {
            throw  new RuntimeException();
        }
        //将图片从Redis集合中删除
        jedisPool.getResource().srem(RedisConstant.SETMEAL_PIC_DB_RESOURCES, fileName);
        jedisPool.getResource().srem(RedisConstant.SETMEAL_PIC_RESOURCES, fileName);

    }

    //根据套餐ID查询关联的检查组ID
    @Override
    public List<Integer> findCheckGroupIdsBySetmealId(Integer id) {
        List<Integer> checkgroupIds = setmealDao.findCheckGroupIdsBySetmealId(id);

        return checkgroupIds;
    }

    @Override
    //根据id查找套餐
    public Setmeal findById(Integer id) {
        Setmeal setmeal = setmealDao.selectById(id);
        return setmeal;
    }


    //查询所有
    @Override
    public List<Setmeal> findAll() {
        return setmealDao.selectAll();
    }

    //设置套餐和检查组多对多关系,操作t_setmeal_checkgroup
    public void addSetmealAndCheckGroup(Integer setmealId, Integer[] checkGroupIds) {
        if (checkGroupIds != null && checkGroupIds.length > 0) {
            for (Integer checkGroupId : checkGroupIds) {
                Map<String, Integer> map = new HashMap<>();
                map.put("setmealId", setmealId);
                map.put("checkGroupId", checkGroupId);
                setmealDao.addSetmealAndCheckGroup(map);
            }
        }
    }
}

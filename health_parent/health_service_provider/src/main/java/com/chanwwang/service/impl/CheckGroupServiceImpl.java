package com.chanwwang.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.chanwwang.dao.CheckGroupDao;
import com.chanwwang.entity.PageResult;
import com.chanwwang.entity.QueryPageBean;
import com.chanwwang.pojo.CheckGroup;
import com.chanwwang.pojo.CheckItem;
import com.chanwwang.service.CheckGroupService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ChanwWang
 * @version 1.0
 */
@Service(interfaceClass = CheckGroupService.class)
@Transactional
public class CheckGroupServiceImpl implements CheckGroupService {

    @Autowired
    private CheckGroupDao checkGroupDao;

    //新增检查组,同时需要人检查组关联检查项
    @Override
    public void add(CheckGroup checkGroup, Integer[] checkitemIds) {
        //新增检查组,操作t_checkgroup表
        checkGroupDao.add(checkGroup);

        //设置检查组和检查项的多对多的关联关系,操作t_checkgroup_checkitem表
        //# 通过selectKey标签 获得自增的id的值 让id赋值到checkGroup的id属性上
        Integer checkGroupId = checkGroup.getId();

    }

    @Override
    //检查组分页查询
    public PageResult pageQuery(QueryPageBean queryPageBean) {
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        String queryString = queryPageBean.getQueryString();

        //完成分页查询,基于mybatis框架提供的分页助手插件完成
        PageHelper.startPage(currentPage, pageSize);
        Page<CheckGroup> page = checkGroupDao.selectByCondition(queryString);

        long total = page.getTotal();
        List<CheckGroup> rows = page.getResult();

        return new PageResult(total, rows);
    }

    @Override
    //根据id查找检查组
    public CheckGroup findById(Integer id) {
        CheckGroup checkGroup = checkGroupDao.selectById(id);
        return checkGroup;
    }

    //根据检查组ID查询关联的检查项ID
    @Override
    public List<Integer> findCheckItemIdsByCheckGroupId(Integer id) {
        List<Integer> checkItemIds = checkGroupDao.selectCheckItemIdsByCheckGroupId(id);

        return checkItemIds;
    }

    //    更新检查组数据
    @Override
    public boolean update(CheckGroup checkGroup, Integer[] checkitemIds) {
        //修改检查组基本信息,操作检查组t_checkgroup表
        //是否修改成功
        boolean flag = checkGroupDao.update(checkGroup) > 0;
        Integer checkGroupId = checkGroup.getId();

        //清理当前检查组关联的检查项,操作中间关系表 t_checkgroup_checkitem表
        checkGroupDao.deleteAssociation(checkGroupId);
        //重新建立当前检查组和检查项的关联关系

        setCheckGroupAndCheckItem(checkGroupId, checkitemIds);
        return flag;
    }

    //根据检查组id删除检查组项
    @Override
    public void deleteById(Integer id) {

         boolean flag = checkGroupDao.deleteById(id)>0;
        if ( !flag ) {
            throw  new RuntimeException();
        }

    }

    //建立检查组和检查项多对多关系
    public void setCheckGroupAndCheckItem(Integer checkGroupId, Integer[] checkitemIds) {
        if (checkitemIds != null && checkitemIds.length > 0) {
            for (Integer checkItemId : checkitemIds) {
                Map<String, Integer> map = new HashMap<>();
                map.put("checkGroupId", checkGroupId);
                map.put("checkItemId", checkItemId);
                checkGroupDao.setCheckGroupAndCheckItem(map);
            }
        }
    }

    //查询所有
    @Override
    public List<CheckGroup> findAll() {
        return checkGroupDao.selectAll();
    }
}

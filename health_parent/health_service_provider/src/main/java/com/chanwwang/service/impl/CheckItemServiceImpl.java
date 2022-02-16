package com.chanwwang.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.chanwwang.dao.CheckItemDao;
import com.chanwwang.entity.PageResult;
import com.chanwwang.entity.QueryPageBean;
import com.chanwwang.pojo.CheckItem;
import com.chanwwang.service.CheckItemService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author ChanwWang
 * @version 1.0
 * 检查项服务
 */
@Service(interfaceClass = CheckItemService.class)
@Transactional
public class CheckItemServiceImpl implements CheckItemService {
    //注入DAO对象
    @Autowired
    private CheckItemDao checkItemDao;

    @Override
    public void add(CheckItem checkItem) {
        checkItemDao.add(checkItem);
    }

    @Override
    //检查项分页查询
    public PageResult pageQuery(QueryPageBean queryPageBean) {
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        String queryString = queryPageBean.getQueryString();
        //完成分页查询,基于mybatis框架提供的分页助手插件完成

        PageHelper.startPage(currentPage,pageSize);
        Page<CheckItem> page = checkItemDao.selectByCondition(queryString);
        long total = page.getTotal();
        List<CheckItem> rows = page.getResult();

        return new PageResult(total,rows);
    }


    //按ID删除
    @Override
    public void deleteById(Integer id) {
        if (id == null) {
            throw new RuntimeException();
        }
        //判断当前检查项是否已经关联到检查组
        long countByCheckItemId = checkItemDao.findCountByCheckItemId(id);
        if (countByCheckItemId > 0) {
            //当前检查项已经被关联到检查组,不允许删除
            throw new RuntimeException();
        }

        checkItemDao.deleteById(id);
    }

    @Override
    public CheckItem findById(Integer id) {
        CheckItem checkItem = checkItemDao.selectById(id);
        return checkItem;
    }

    @Override
    public boolean update(CheckItem checkItem) {

        return checkItemDao.update(checkItem) > 0;
    }

    @Override
    public List<CheckItem> findAll() {
        return checkItemDao.selectAll();
    }
}

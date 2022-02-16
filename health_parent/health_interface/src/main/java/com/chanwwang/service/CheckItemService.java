package com.chanwwang.service;

import com.chanwwang.entity.PageResult;
import com.chanwwang.entity.QueryPageBean;
import com.chanwwang.pojo.CheckItem;

import java.util.List;

/**
 * @author ChanwWang
 * @version 1.0
 * 服务接口
 */
public interface CheckItemService {
    public void add(CheckItem checkItem);

    public PageResult pageQuery(QueryPageBean queryPageBean);

    public void deleteById(Integer id);

    public CheckItem findById(Integer id);

    public boolean update(CheckItem checkItem);

    public List<CheckItem> findAll();


}

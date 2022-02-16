package com.chanwwang.service;

import com.chanwwang.entity.PageResult;
import com.chanwwang.entity.QueryPageBean;
import com.chanwwang.pojo.CheckGroup;

import java.util.List;

/**
 * @author ChanwWang
 * @version 1.0
 */
public interface CheckGroupService {

    public void add(CheckGroup checkGroup, Integer[] checkitemIds);

    public PageResult pageQuery(QueryPageBean queryPageBean);

    public CheckGroup findById(Integer id);

    public List<Integer> findCheckItemIdsByCheckGroupId(Integer id);

    public boolean update(CheckGroup checkGroup,Integer[] checkitemIds);

}

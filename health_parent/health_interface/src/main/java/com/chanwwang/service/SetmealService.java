package com.chanwwang.service;

import com.chanwwang.entity.PageResult;
import com.chanwwang.entity.QueryPageBean;
import com.chanwwang.pojo.CheckGroup;
import com.chanwwang.pojo.Setmeal;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author ChanwWang
 * @version 1.0
 */
public interface SetmealService {

    public void add(Setmeal setmeal, Integer[] checkgroupIds);

    public PageResult pageQuery(QueryPageBean queryPageBean);

    public Setmeal findById(Integer id);

    public Setmeal findById4Detail(Integer id);

    public List<Setmeal> findAll();

    public List<Integer> findCheckGroupIdsBySetmealId(Integer id);

    public boolean update(Setmeal setmeal,Integer[] checkgroupIds);

    public void deleteById(Integer id);

    public List<Map<String,Object>> findSetmealCount();
}

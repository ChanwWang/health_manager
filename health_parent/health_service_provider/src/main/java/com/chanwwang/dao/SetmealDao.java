package com.chanwwang.dao;

import com.chanwwang.pojo.CheckGroup;
import com.chanwwang.pojo.Setmeal;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author ChanwWang
 * @version 1.0
 */

public interface SetmealDao {

    public void add(Setmeal setmeal);

    public Integer update(Setmeal setmeal);
    //根据id删除检查组
    public Integer deleteById(Integer id);

    public void addSetmealAndCheckGroup(Map map);

    public Page<Setmeal> selectByCondition(@Param("queryString") String queryString);

    public Setmeal selectById(Integer id);

    public List<Setmeal> selectAll();

    public List<Integer> findCheckGroupIdsBySetmealId(Integer setmealId);



    //根据套餐id 清理当前套餐关联的检查组,操作中间关系表
    public Integer deleteAssociation(Integer id);
}

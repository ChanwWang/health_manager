package com.chanwwang.dao;

import com.chanwwang.pojo.CheckGroup;
import com.chanwwang.pojo.CheckItem;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * @author ChanwWang
 * @version 1.0
 */
public interface CheckGroupDao {

    //新增检查组
    public void add(CheckGroup checkGroup);


    public Page<CheckGroup> selectByCondition(@Param("queryString") String queryString);



    public void setCheckGroupAndCheckItem(Map map);

    public CheckGroup selectById(Integer id);

    @Select("select count(*) from t_checkgroup_checkitem where checkgroup_id=#{checkGroupId}")
    public List<Integer> selectCheckItemIdsByCheckGroupId(Integer checkGroupId);


    public Integer update(CheckGroup checkGroup);

}

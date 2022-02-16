package com.chanwwang.dao;

import com.chanwwang.pojo.CheckItem;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author ChanwWang
 * @version 1.0
 */
public interface CheckItemDao {


    public void add(CheckItem checkItem);

    public Page<CheckItem> selectByCondition(@Param("queryString") String queryString);

    @Delete("delete from t_checkitem where id = #{id}")
    public void deleteById(Integer id);

    //检测是否在关联表里关联了检查组
    public long findCountByCheckItemId(Integer id);


    public CheckItem selectById(Integer id);

    public Integer update(CheckItem checkItem);

    public List<CheckItem> selectAll();
}

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

    //根据id删除检查组
    public Integer deleteById(Integer id);

    public Page<CheckGroup> selectByCondition(@Param("queryString") String queryString);



    public void setCheckGroupAndCheckItem(Map map);

    public CheckGroup selectById(Integer id);



    public List<Integer> selectCheckItemIdsByCheckGroupId(Integer checkGroupId);


    public Integer update(CheckGroup checkGroup);

    //根据检查组id 清理当前检查组关联的检查项,操作中间关系表 t_checkgroup_checkitem表
    public Integer deleteAssociation(Integer id);

    //查询所有
    public List<CheckGroup> selectAll();

}

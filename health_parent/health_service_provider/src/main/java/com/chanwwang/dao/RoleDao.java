package com.chanwwang.dao;

import com.chanwwang.pojo.Role;
import org.apache.ibatis.annotations.Param;

import java.util.Set;

/**
 * @author ChanwWang
 * @version 1.0
 */
public interface RoleDao {

    public Set<Role> findByUserId(@Param("userId") Integer userId);
}

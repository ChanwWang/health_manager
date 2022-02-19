package com.chanwwang.dao;

import com.chanwwang.pojo.Permission;
import org.apache.ibatis.annotations.Param;

import java.util.Set;

/**
 * @author ChanwWang
 * @version 1.0
 */
public interface PermissionDao {

    public Set<Permission> findByRoleId(@Param("roleId") Integer roleId);
}

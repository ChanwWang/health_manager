package com.chanwwang.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.chanwwang.dao.PermissionDao;
import com.chanwwang.dao.RoleDao;
import com.chanwwang.dao.UserDao;
import com.chanwwang.pojo.Permission;
import com.chanwwang.pojo.Role;
import com.chanwwang.pojo.User;
import com.chanwwang.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

/**
 * @author ChanwWang
 * @version 1.0
 */
@Service(interfaceClass = UserService.class)
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private PermissionDao permissionDao;

    //根据用户名查询数据库获取用户信息和关联的角色信息,同时需要查询角色关联的权限信息
    public User findByUsername(String username) {
        User user = userDao.findByUsername(username);//查询用户基本信息,不包含用户的角色
        if (user == null) {
            return null;
        }
        Integer userId = user.getId();

        //根据用户Id查询对应的角色
        Set<Role> roles = roleDao.findByUserId(userId);

        if (roles != null && roles.size() >0) {
            for (Role role : roles) {
                Integer roleId = role.getId();
                //根据角色ID查询关联的权限
                Set<Permission> permissions = permissionDao.findByRoleId(roleId);
                if (permissions != null && permissions.size() > 0) {
                    role.setPermissions(permissions);
                }
            }
            user.setRoles(roles);
        }
        return user;
    }
}

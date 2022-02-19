package com.chanwwang.service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.chanwwang.pojo.Permission;
import com.chanwwang.pojo.Role;
import com.chanwwang.pojo.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author ChanwWang
 * @version 1.0
 */
@Component
public class SpringSecurityUserService implements UserDetailsService {
    @Reference//此处要通过dubbo远程调用用户服务
    private UserService userService;

    //根据用户名查询数据库获取用户信息
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //远程调用用户服务，根据用户名查询用户信息
        User user = userService.findByUsername(username);
        if (user == null) {
            //用户名不存在
            return null;
        }

        List<GrantedAuthority> list = new ArrayList<>();
        //动态为当前用户授权
        Set<Role> roles = user.getRoles();
        for (Role role : roles) {
            //遍历角色集合,为用户授予角色
            list.add(new SimpleGrantedAuthority(role.getKeyword()));
            Set<Permission> permissions = role.getPermissions();
            //遍历权限集合 ,为用户授权
            for (Permission permission : permissions) {
                list.add(new SimpleGrantedAuthority(permission.getKeyword()));
            }
        }
        UserDetails securityUser =
                new org.springframework.security.core.userdetails.User
                        (username,user.getPassword(),list);
        return securityUser;
    }
}

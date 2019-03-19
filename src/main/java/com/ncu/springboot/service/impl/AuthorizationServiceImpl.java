package com.ncu.springboot.service.impl;

import com.ncu.springboot.service.AuthorizationService;
import com.ncu.springboot.dao.PermissionDao;
import com.ncu.springboot.dao.RoleDao;
import com.ncu.springboot.dao.UserDao;
import com.ncu.springboot.pojo.Permission;
import com.ncu.springboot.pojo.Role;
import com.ncu.springboot.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthorizationServiceImpl implements AuthorizationService {
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private PermissionDao permissionDao;
    @Override
    public List<Role> findUserRolePermissions(Long userId) {
        List<Role> roles = roleDao.selectRoleByUser(userId);
        for(Role role : roles){
            role.setPermissions(permissionDao.selectPermissionByRole(role.getId()));
        }
        return roles;
    }



    @Override
    public User findUserRolePermissionsByName(String name) {
        User user = userDao.selectUserByName(name);
        if(user == null) {
            return null;
        }
        List<Role> roles = findUserRolePermissions(user.getId());
        user.setRoles(new HashSet<>(roles));
        return user;
    }

    @Override
    public Set<String> findRoles(String username) {
        User user = userDao.selectUserByName(username);
        return findUserRolePermissions(user.getId()).stream().map(Role::getName).collect(Collectors.toSet());
    }

    @Override
    public Set<String> findPermissions(String username) {
        User user = userDao.selectUserByName(username);
        List<Role> userRolePermissions = findUserRolePermissions(user.getId());
        Set<Permission> permissions = new HashSet<>();
        for (Role role : userRolePermissions){
            permissions.addAll(role.getPermissions());
        }
        return permissions.stream().map(Permission::getName).collect(Collectors.toSet());
    }
}

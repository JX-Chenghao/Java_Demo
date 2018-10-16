package com.ncu.springboot.Service.impl;

import com.ncu.springboot.Service.AuthorizationService;
import com.ncu.springboot.dao.PermissionDao;
import com.ncu.springboot.dao.RoleDao;
import com.ncu.springboot.pojo.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class AuthorizationServiceImpl implements AuthorizationService {
    @Autowired
    private RoleDao roleDao;
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
}

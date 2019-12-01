package com.ncu.springboot.service.impl;

import com.ncu.springboot.associatetable.AssociateDao;
import com.ncu.springboot.associatetable.AssociateTable;
import com.ncu.springboot.dao.PermissionDao;
import com.ncu.springboot.dao.RoleDao;
import com.ncu.springboot.pojo.Permission;
import com.ncu.springboot.pojo.Role;
import com.ncu.springboot.service.UserService;
import com.ncu.springboot.dao.UserDao;
import com.ncu.springboot.mvc.helper.PasswordHelper;
import com.ncu.springboot.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private PasswordHelper passwordHelper;
    @Autowired
    private AssociateDao associateDao;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private PermissionDao permissionDao;
/*
    //  构造函数的调用 是在 注入对象 注入之前，此时我们使用bean会出错
    //  Constructor >> @Autowired >> @PostConstruct
    protected UserServiceImpl(){
        System.out.println("Chenghao--"+userDao.selectUserByName("a"));
    }
*/

    @PostConstruct
    private void init (){
        //会在userDao Spring容器注入对象 注入之后，自动调用此方法
        System.out.println("Chenghao-测试@PostConstruct注解-"+userDao.selectUserByName("a"));
    }

    @Override
    public User findUserByName(String name) {
        User user = userDao.selectUserByName(name);
        if (user == null) return null;
        List<Long> roleIds = associateDao.listByAssoc(AssociateTable.USER_ROLE, user.getId());
        HashSet<Role> roles = new HashSet<>();
        roleIds.forEach(roleId -> {
            Role role = roleDao.selectById(roleId);
            HashSet<Permission> permissions = new HashSet<>();
            if (role != null) {
                List<Long> permissionIds = associateDao.listByAssoc(AssociateTable.ROLE_PERMISSION, role.getId());
                permissionIds.forEach(permissionId-> {
                    Permission permission = permissionDao.selectById(permissionId);
                    if (permission != null) permissions.add(permission);
                });
                role.setPermissions(permissions);
                roles.add(role);
            }
        });
        user.setRoles(roles);
        return user;
    }

    @Override
    public void saveUser(User user) {
         passwordHelper.encryptPassword(user);
         userDao.insert(user);
         //throw new OwnException("校验事务回滚");
    }

}

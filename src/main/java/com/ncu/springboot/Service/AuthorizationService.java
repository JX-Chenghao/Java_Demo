package com.ncu.springboot.Service;

import com.ncu.springboot.pojo.Role;
import com.ncu.springboot.pojo.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

public interface AuthorizationService {
    List<Role> findUserRolePermissions(Long userId);
    User findUserRolePermissionsByName(String name);

    Set<String> findRoles(String username);
    Set<String> findPermissions(String username);
}

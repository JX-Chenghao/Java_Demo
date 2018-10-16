package com.ncu.springboot.Service;

import com.ncu.springboot.pojo.Role;
import org.springframework.stereotype.Service;

import java.util.List;

public interface AuthorizationService {
    List<Role> findUserRolePermissions(Long userId);
}

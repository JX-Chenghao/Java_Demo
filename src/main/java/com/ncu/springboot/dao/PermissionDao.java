package com.ncu.springboot.dao;

import com.ncu.springboot.pojo.Permission;
import org.apache.ibatis.annotations.Mapper;

import java.util.Set;

@Mapper
public interface PermissionDao {
	Set<Permission> selectPermissionByRole(Long roleId);
}

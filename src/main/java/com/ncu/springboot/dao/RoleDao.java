package com.ncu.springboot.dao;

import com.ncu.springboot.pojo.Role;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RoleDao {
	List<Role> selectRoleByUser(Long userId);
}

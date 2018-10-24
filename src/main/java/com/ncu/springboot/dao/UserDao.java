package com.ncu.springboot.dao;

import com.ncu.springboot.pojo.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserDao {
    User selectUserByName(String name);

    void insert(User user);
}

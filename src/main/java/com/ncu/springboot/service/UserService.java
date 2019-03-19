package com.ncu.springboot.service;

import com.ncu.springboot.pojo.User;

public interface UserService {
    User findUserByName(String name);

    void saveUser(User user);
}

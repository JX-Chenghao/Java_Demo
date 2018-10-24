package com.ncu.springboot.Service.impl;

import com.ncu.springboot.Service.UserService;
import com.ncu.springboot.dao.UserDao;
import com.ncu.springboot.mvc.helper.PasswordHelper;
import com.ncu.springboot.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private PasswordHelper passwordHelper;

    @Override
    public User findUserByName(String name) {
        return userDao.selectUserByName(name);
    }

    @Override
    public void saveUser(User user) {
         passwordHelper.encryptPassword(user);
         userDao.insert(user);
    }

}

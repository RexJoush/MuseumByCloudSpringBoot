package com.nwu.service.impl;

import com.nwu.dao.UserDao;
import com.nwu.entity.User;
import com.nwu.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Rex Joush
 * @time 2021.03.18
 */
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserDao userDao;

    /**
     * 用户登录接口
     * @return 查询登录的用户
     */
    public User login(String username, String password){
        return userDao.login(username, password);
    }

    @Override
    public List<User> findAll(int pageStart, int pageSize) {
        return userDao.findAll(pageStart, pageSize);
    }

    @Override
    public int getUserAmount() {
        return userDao.getUserAmount();
    }
}

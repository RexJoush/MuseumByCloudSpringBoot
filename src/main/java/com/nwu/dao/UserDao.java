package com.nwu.dao;

import com.nwu.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author Rex Joush
 * @time 2021.03.18
 */
@Mapper
public interface UserDao {

    /**
     * 用户登录接口
     * @return 查询登录的用户
     */
    User login(String username, String password);

    /**
     * 查询所有用户
     * @return 用户列表
     */
    List<User> findAll(int pageStart, int pageSize);

    /**
     * 根据用户名查询用户
     * @return 用户
     */
    User findByUsername(String username);

    /**
     * 获取用户的数量
     * @return 用户数量
     */
    int getUserAmount();

}

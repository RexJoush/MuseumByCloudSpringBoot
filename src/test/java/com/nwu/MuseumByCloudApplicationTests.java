package com.nwu;

import com.nwu.dao.UserDao;
import com.nwu.entity.User;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class MuseumByCloudApplicationTests {

    @Resource
    private UserDao userDao;


    void contextLoads() {
        User admin = userDao.findByUsername("admin");
        System.out.println(admin);
    }

}

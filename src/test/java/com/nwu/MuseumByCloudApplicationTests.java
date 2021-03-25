package com.nwu;

import com.nwu.dao.UserDao;
import com.nwu.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class MuseumByCloudApplicationTests {

    @Resource
    private UserDao userDao;

    @Test
    void contextLoads() {
        User admin = userDao.findByUsername("admin");
        System.out.println(admin);
    }

}

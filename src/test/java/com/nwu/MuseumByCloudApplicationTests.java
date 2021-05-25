package com.nwu;

import com.nwu.dao.UserDao;
import com.nwu.service.impl.CustomizeServiceImpl;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class MuseumByCloudApplicationTests {

    @Resource
    private UserDao userDao;

    @Resource
    private CustomizeServiceImpl customizeService;


}

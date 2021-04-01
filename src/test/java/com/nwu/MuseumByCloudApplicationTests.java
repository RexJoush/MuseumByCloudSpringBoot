package com.nwu;

import com.nwu.dao.UserDao;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class MuseumByCloudApplicationTests {

    @Resource
    private UserDao userDao;


    void contextLoads() {
//        NodeMetricsList metricsList = KubernetesUtils.client.top().nodes().metrics();
//        for (NodeMetrics metrics : metricsList.getItems()) {
//            System.out.println(metrics);
//        }
    }

}

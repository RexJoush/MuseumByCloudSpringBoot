package com.nwu;

import com.nwu.dao.UserDao;
import com.nwu.entity.User;
<<<<<<< HEAD
=======
import com.nwu.util.KubernetesUtils;
import io.fabric8.kubernetes.api.model.metrics.v1beta1.NodeMetrics;
import io.fabric8.kubernetes.api.model.metrics.v1beta1.NodeMetricsList;
import org.junit.jupiter.api.Test;
>>>>>>> 47971e4d6e61b39e62c133cb5eedafd21f44f55d
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

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

package com.nwu;

import com.alibaba.fastjson.JSON;
import com.nwu.dao.UserDao;
import com.nwu.entity.User;
import com.nwu.service.impl.CustomizeServiceImpl;
import com.nwu.util.KubernetesUtils;
import io.fabric8.kubernetes.api.model.VolumeDevice;
import io.fabric8.kubernetes.api.model.metrics.v1beta1.NodeMetrics;
import io.fabric8.kubernetes.api.model.metrics.v1beta1.NodeMetricsList;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

@SpringBootTest
class MuseumByCloudApplicationTests {

    @Resource
    private UserDao userDao;

    @Resource
    private CustomizeServiceImpl customizeService;

    @Test
    void contextLoads() throws FileNotFoundException {
        List<Map<Object, Object>> devices = (List<Map<Object, Object>>) customizeService.getCustomResourceDefinitionObjectListByName("devices.devices.kubeedge.io").get("items");

        Map<Object, Object> metadata = (Map<Object, Object>) devices.get(0).get("metadata");
        System.out.println(metadata.get("name"));
        System.out.println(metadata.get("namespace"));

    }

}

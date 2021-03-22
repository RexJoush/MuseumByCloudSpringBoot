package com.nwu.controller.workload;

import com.alibaba.fastjson.JSON;
import com.nwu.service.workload.impl.PodsServiceImpl;
import io.fabric8.kubernetes.api.model.Pod;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.models.V1PodList;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Rex Joush
 * @time 2021.03.22
 */

/**
 * Pods 的 controller 层
 */
@RequestMapping("/pods")
@RestController
public class PodsController {

    @Resource
    private PodsServiceImpl podsService;

    @RequestMapping("getAllPods")
    public String findAllPods() throws ApiException {

        List<Pod> pods = podsService.findAllPods();

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "获取 Pod 列表成功");
        result.put("data", pods);

        return JSON.toJSONString(result);

    }

    @RequestMapping("getPodsByNamespace")
    public String findPodsByNamespace(String namespace) throws ApiException {

        List<Pod> v1PodList = podsService.findPodsByNamespace(namespace);

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "获取 Pod 列表成功");
        result.put("data", v1PodList);

        return JSON.toJSONString(result);

    }

}

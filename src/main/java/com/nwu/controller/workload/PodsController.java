package com.nwu.controller.workload;

import com.alibaba.fastjson.JSON;
import com.nwu.entity.workload.PodDefinition;
import com.nwu.entity.workload.PodDetails;
import com.nwu.service.workload.PodsService;
import com.nwu.service.workload.impl.PodsServiceImpl;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.batch.CronJob;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.models.V1PodList;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
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

    @RequestMapping("/getAllPods")
    public String findAllPods(String namespace) {

        List<PodDefinition> pods;
        if ("all".equals(namespace)){
            pods = podsService.findAllPods();
        } else {
            pods = podsService.findPodsByNamespace(namespace);
        }


        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "获取 Pod 列表成功");
        result.put("data", pods);

        return JSON.toJSONString(result);

    }

    @RequestMapping("/getPodsByNode")
    public String findPodsByNode(String nodeName) {

        List<PodDefinition> pods = podsService.findPodsByNode(nodeName);

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "获取 Pod 列表成功");
        result.put("data", pods);

        return JSON.toJSONString(result);
    }

    @RequestMapping("/getPodByNameAndNamespace")
    public String findPodByNameAndNamespace(String name, String namespace) {
        System.out.println("name, " + name);
        System.out.println("namespace, " + namespace);
        PodDetails podByNameAndNamespace = podsService.findPodByNameAndNamespace(name, namespace);

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "获取 Pod 详情成功");
        result.put("data", podByNameAndNamespace);

        return JSON.toJSONString(result);
    }

    @RequestMapping("/getPodsByNamespace")
    public String findPodsByNamespace(String namespace) throws ApiException {

        List<PodDefinition> v1PodList = podsService.findPodsByNamespace(namespace);

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "获取 Pod 列表成功");
        result.put("data", v1PodList);

        return JSON.toJSONString(result);

    }

    @RequestMapping("/deletePodByNameAndNamespace")
    public String deletePodByNameAndNamespace(String name, String namespace){
        Boolean delete = podsService.deletePodByNameAndNamespace(name, namespace);

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "删除 Pod 成功");
        result.put("data", delete);

        return JSON.toJSONString(result);
    }

    @RequestMapping("/loadPodFromYaml")
    public String loadPodFromYaml(String path) throws FileNotFoundException {

        Pod aPod = podsService.loadPodFromYaml(path);

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "加载 Pod 成功");
        result.put("data", aPod);

        return JSON.toJSONString(result);
    }

    @RequestMapping("/createPodFromYaml")
    public String createPodFromYaml(String path) throws FileNotFoundException {

        Pod aPod = podsService.createPodByYaml(path);

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "创建 Pod 成功");
        result.put("data", aPod);

        return JSON.toJSONString(result);
    }

    @RequestMapping("/createOrReplacePod")
    public String createOrReplacePod(String path) throws FileNotFoundException {
        Pod aPod = podsService.createOrReplacePod(path);

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "创建或更新 Pod 成功");
        result.put("data", aPod);

        return JSON.toJSONString(result);
    }

    @RequestMapping("/getPodLogByNameAndNamespace")
    public String getPodLogByNameAndNamespace(String name, String namespace){
        String str = podsService.getPodLogByNameAndNamespace(name, namespace);

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "获取 Pod日志 成功");
        result.put("data", str);

        return JSON.toJSONString(result);
    }

    @RequestMapping("/createPodFromForm")
    public String createPodFromForm(String name, String namespace, Map<String, String> labels, Map<String, String> annotations,
                                    String secretName, String images, String imagePullPolicy, String[] command, String[] args,
                                    String cpuLimit, String cpuRequest, String memoryLimit, String memoryRequest, Map<String, String> envVar, Integer amount) {
        List<Pod> podList = podsService.createPodFromForm(name, namespace, labels, annotations,
                secretName, images, imagePullPolicy, command, args,
                cpuLimit, cpuRequest, memoryLimit, memoryRequest, envVar, amount);

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "创建 Pod 成功");
        result.put("data", podList);

        return JSON.toJSONString(result);

    }

}

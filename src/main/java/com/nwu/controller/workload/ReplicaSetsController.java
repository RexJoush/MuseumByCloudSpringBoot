package com.nwu.controller.workload;

/**
 * @author Rex Joush
 * @time 2021.03.22
 */

import com.alibaba.fastjson.JSON;
import com.nwu.service.explorebalancing.impl.ServicesServiceImpl;
import com.nwu.service.impl.CommonServiceImpl;
import com.nwu.service.workload.impl.PodsServiceImpl;
import com.nwu.service.workload.impl.ReplicaSetsServiceImpl;
import com.nwu.util.FilterPodsByControllerUid;
import com.nwu.util.format.PodFormat;
import com.nwu.util.format.ReplicaSetFormat;
import io.fabric8.kubernetes.api.model.Event;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.Service;
import io.fabric8.kubernetes.api.model.apps.ReplicaSet;
import io.kubernetes.client.openapi.ApiException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Replica Sets 的 controller 层
 */
@RestController
@RequestMapping("/replicaSets")
public class ReplicaSetsController {
    @Resource
    private ReplicaSetsServiceImpl replicaSetsService;

    @RequestMapping("/getAllReplicaSets")
    public String findAllReplicaSets(String namespace) throws ApiException {

        List<ReplicaSet> replicaSetList;

        if("".equals(namespace)){
            replicaSetList = replicaSetsService.findAllReplicaSets();
        }else {
            replicaSetList = replicaSetsService.findReplicaSetsByNamespace(namespace);
        }

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "获取 ReplicaSet 列表成功");
        result.put("data", ReplicaSetFormat.formatReplicaSetList(replicaSetList));

        return JSON.toJSONString(result);

    }

    @RequestMapping("/getReplicaSetsByNamespace")
    public String findReplicaSetsByNamespace(String namespace) throws ApiException {

        List<ReplicaSet> replicaSetList = replicaSetsService.findReplicaSetsByNamespace(namespace);

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "获取 ReplicaSet 列表成功");
        result.put("data", replicaSetList);

        return JSON.toJSONString(result);

    }

    @RequestMapping("/getReplicaSetByNameAndNamespace")
    public String getReplicaSetByNameAndNamespace(String name, String namespace){

        ReplicaSet replicaSet = replicaSetsService.getReplicaSetByNameAndNamespace(name, namespace);

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "根据名称和命名空间获取 ReplicaSet 成功");
        result.put("data", replicaSet);

        return JSON.toJSONString(result);
    }

    @RequestMapping("/deleteReplicaSetByNameAndNamespace")
    public String deleteReplicaSetByNameAndNamespace(String name, String namespace){
        Boolean delete = replicaSetsService.deleteReplicaSetByNameAndNamespace(name, namespace);

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "删除 ReplicaSet 成功");
        result.put("data", delete);

        return JSON.toJSONString(result);
    }

    @RequestMapping("/loadReplicaSetFromYaml")
    public String loadReplicaSetFromYaml(String path) throws FileNotFoundException {

        ReplicaSet aReplicaSet = replicaSetsService.loadReplicaSetFromYaml(path);

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "加载 ReplicaSet 成功");
        result.put("data", aReplicaSet);

        return JSON.toJSONString(result);
    }

    @RequestMapping("/createReplicaSetFromYaml")
    public String createReplicaSetFromYaml(String path) throws FileNotFoundException {

        ReplicaSet aReplicaSet = replicaSetsService.createReplicaSetByYaml(path);

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "创建 ReplicaSet 成功");
        result.put("data", aReplicaSet);

        return JSON.toJSONString(result);
    }

    @RequestMapping("/createOrReplaceReplicaSet")
    public String createOrReplaceReplicaSet(String path) throws FileNotFoundException {
        ReplicaSet aReplicaSet = replicaSetsService.createOrReplaceReplicaSet(path);

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "创建或更新 ReplicaSet 成功");
        result.put("data", aReplicaSet);

        return JSON.toJSONString(result);
    }

    @RequestMapping("/setReplicas")
    public String setReplicas(String name, String namespace, Integer replicas){

        replicaSetsService.setReplicas(name, namespace, replicas);

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "创建或更新 ReplicaSet 成功");
        result.put("data", "未明确");

        return JSON.toJSONString(result);
    }

    @RequestMapping("/getReplicaSetYamlByNameAndNamespace")
    public String getReplicaSetYamlByNameAndNamespace(String name, String namespace){

        String replicaSetYaml = replicaSetsService.getReplicaSetYamlByNameAndNamespace(name, namespace);
        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "获取 ReplicaSet Yaml 成功");
        result.put("data", replicaSetYaml);

        return JSON.toJSONString(result);
    }

    @RequestMapping("/getReplicaSetResources")
    public String getReplicaSetResources(String name,String namespace){

        //获取 ReplicaSet
        ReplicaSet replicaSet = replicaSetsService.getReplicaSetByNameAndNamespace(name, namespace);
        String uid = replicaSet.getMetadata().getUid();
        Map<String, String> matchLabels = replicaSet.getSpec().getSelector().getMatchLabels();

        //获取 Pods
        PodsServiceImpl podsService = new PodsServiceImpl();
        List<Pod> pods = FilterPodsByControllerUid.filterPodsByControllerUid(uid, podsService.findPodsByLabels(matchLabels));

        //获取 Services
        ServicesServiceImpl servicesService = new ServicesServiceImpl();
        List<Service> services = servicesService.getServicesByLabels(matchLabels);

        //获取事件
        List<Event> events = CommonServiceImpl.getEventByInvolvedObjectUid(replicaSet.getMetadata().getUid());

        //封装数据
        Map<String, Object> data = new HashMap<>();
        data.put("replicaSet", replicaSet);
        data.put("pods", PodFormat.formatPodList(pods));
        data.put("services", services);
        data.put("events", events);

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "获取 ReplicaSet Resources 成功");
        result.put("data", data);

        return JSON.toJSONString(result);
    }

    @RequestMapping("/getReplicaSetLogs")
    public String getReplicaSetLogs(String name ,String namespace){

        //获取 ReplicaSet 包含的 Pods
        List<Pod> pods = replicaSetsService.getPodReplicaSetInvolved(name, namespace);
        // 获取每个 Pod 的所有 Logs
        Map<String, Map<String, String>> podLogs = new HashMap<>();
        PodsServiceImpl podsService = new PodsServiceImpl();
        for(int i = 0; i < pods.size(); i++){
            podLogs.put(pods.get(i).getMetadata().getName(), podsService.getPodAllLogs(pods.get(i).getMetadata().getName(), namespace));
        }

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "获取 ReplicaSet 日志成功");
        result.put("data", podLogs);

        return JSON.toJSONString(result);
    }
}

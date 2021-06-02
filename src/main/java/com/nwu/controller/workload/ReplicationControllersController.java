package com.nwu.controller.workload;

/**
 * @author Rex Joush
 * @time 2021.03.22
 */

import com.alibaba.fastjson.JSON;
import com.nwu.service.explorebalancing.impl.ServicesServiceImpl;
import com.nwu.service.impl.CommonServiceImpl;
import com.nwu.service.workload.impl.PodsServiceImpl;
import com.nwu.service.workload.impl.ReplicationControllersServiceImpl;
import com.nwu.util.FilterPodsByControllerUid;
import com.nwu.util.format.PodFormat;
import com.nwu.util.format.ReplicationControllerFormat;
import io.fabric8.kubernetes.api.model.Event;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.ReplicationController;
import io.fabric8.kubernetes.api.model.Service;
import io.kubernetes.client.openapi.ApiException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Replication Controllers 的 controller 层
 */
@RestController
@RequestMapping("/replicationControllers")
public class ReplicationControllersController {

    @Resource
    private ReplicationControllersServiceImpl replicationControllersService;

    //增
    @RequestMapping("/createReplicationControllerFromYaml")
    public String createReplicationControllerFromYaml(String path) throws FileNotFoundException {

        ReplicationController aReplicationController = replicationControllersService.createReplicationControllerByYaml(path);

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "创建 ReplicationController 成功");
        result.put("data", aReplicationController);

        return JSON.toJSONString(result);
    }

    //删
    @RequestMapping("/deleteReplicationControllerByNameAndNamespace")
    public String deleteReplicationControllerByNameAndNamespace(String name, String namespace){

        Boolean delete = replicationControllersService.deleteReplicationControllerByNameAndNamespace(name, namespace);

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "删除 ReplicationController 成功");
        result.put("data", delete);

        return JSON.toJSONString(result);
    }

    //改
    @RequestMapping("/createOrReplaceReplicationController")
    public String createOrReplaceReplicationController(String path) throws FileNotFoundException {

        ReplicationController aReplicationController = replicationControllersService.createOrReplaceReplicationController(path);

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "创建或更新 ReplicationController 成功");
        result.put("data", aReplicationController);

        return JSON.toJSONString(result);
    }
    @RequestMapping("/setReplicas")
    public String setReplicas(String name, String namespace, Integer replicas){

        replicationControllersService.setReplicas(name, namespace, replicas);

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "创建或更新 ReplicaSet 成功");
        result.put("data", "未明确");

        return JSON.toJSONString(result);
    }

    //查
    @RequestMapping("/getAllReplicationControllers")
    public String findAllReplicationControllers(String namespace) throws ApiException {

        List<ReplicationController> replicationControllerList;

        if("".equals(namespace)){
            replicationControllerList = replicationControllersService.findAllReplicationControllers();
        }else {
            replicationControllerList = replicationControllersService.findReplicationControllersByNamespace(namespace);
        }

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "获取 ReplicationController 列表成功");
        result.put("data", ReplicationControllerFormat.formatReplicationControllerList(replicationControllerList));

        return JSON.toJSONString(result);
    }
    @RequestMapping("/getReplicationControllersByNamespace")
    public String findReplicationControllersByNamespace(String namespace) throws ApiException {

        List<ReplicationController> replicationControllerList = replicationControllersService.findReplicationControllersByNamespace(namespace);

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "获取 ReplicationController 列表成功");
        result.put("data", replicationControllerList);

        return JSON.toJSONString(result);
    }
    @RequestMapping("/getReplicationControllerYamlByNameAndNamespace")
    public String getReplicationControllerYamlByNameAndNamespace(String name, String namespace){

        String replicationControllerYaml = replicationControllersService.getReplicationControllerYamlByNameAndNamespace(name, namespace);
        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "获取 ReplicationController Yaml 成功");
        result.put("data", replicationControllerYaml);

        return JSON.toJSONString(result);
    }
    @RequestMapping("/getReplicationControllerResources")
    public String getReplicationControllerResources(String name, String namespace){

        //获取 ReplicationController
        ReplicationController replicationController = replicationControllersService.getReplicationControllerByNameAndNamespace(name, namespace);
        String uid = replicationController.getMetadata().getUid();
        Map<String, String> matchLabel = replicationController.getSpec().getSelector();

        //获取 Pods
        PodsServiceImpl podsService = new PodsServiceImpl();
        List<Pod> pods = FilterPodsByControllerUid.filterPodsByControllerUid(uid, podsService.findPodsByLabels(matchLabel));

        //获取 Services
        ServicesServiceImpl servicesService = new ServicesServiceImpl();
        List<Service> services = servicesService.getServicesByLabels(matchLabel);

        //获取事件
        List<Event> events = CommonServiceImpl.getEventByInvolvedObjectUid(replicationController.getMetadata().getUid());

        //封装数据
        Map<String, Object> data = new HashMap<>();
        data.put("replicationController", replicationController);
        data.put("pods", PodFormat.formatPodList(pods));
        data.put("services", services);
        data.put("events", events);

        Map<String, Object> result = new HashMap<>();
        result.put("code", 1200);
        result.put("message", "获取 ReplicationController Resources 成功");
        result.put("data", data);

        return JSON.toJSONString(result);
    }
    @RequestMapping("/getReplicationControllerLogs")
    public String getReplicationControllerLogs(String name ,String namespace){

        //获取 ReplicationController 包含的 Pods
        List<Pod> pods = replicationControllersService.getPodReplicationControllerInvolved(name, namespace);
        // 获取每个 Pod 的所有 Logs
        Map<String, Map<String, String>> podLogs = new HashMap<>();
        PodsServiceImpl podsService = new PodsServiceImpl();
        for(int i = 0; i < pods.size(); i++){
            podLogs.put(pods.get(i).getMetadata().getName(), podsService.getPodAllLogs(pods.get(i).getMetadata().getName(), namespace));
        }

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "获取 ReplicationController 日志成功");
        result.put("data", podLogs);

        return JSON.toJSONString(result);
    }
}

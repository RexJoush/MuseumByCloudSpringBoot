package com.nwu.controller.workload;

/**
 * @author Rex Joush
 * @time 2021.03.22
 */

import com.alibaba.fastjson.JSON;
import com.nwu.service.explorebalancing.impl.ServicesServiceImpl;
import com.nwu.service.workload.impl.CronJobsServiceImpl;
import com.nwu.service.workload.impl.PodsServiceImpl;
import com.nwu.service.workload.impl.ReplicationControllersServiceImpl;
import com.nwu.util.FilterPodsByControllerUid;
import com.nwu.util.format.PodFormat;
import com.nwu.util.format.ReplicationControllerFormat;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.ReplicationController;
import io.fabric8.kubernetes.api.model.Service;
import io.fabric8.kubernetes.api.model.apps.ReplicaSet;
import io.fabric8.kubernetes.api.model.batch.CronJob;
import io.kubernetes.client.openapi.ApiException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.FileNotFoundException;
import java.io.InputStream;
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

    @RequestMapping("/deleteReplicationControllerByNameAndNamespace")
    public String deleteReplicationControllerByNameAndNamespace(String name, String namespace){
        Boolean delete = replicationControllersService.deleteReplicationControllerByNameAndNamespace(name, namespace);

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "删除 ReplicationController 成功");
        result.put("data", delete);

        return JSON.toJSONString(result);
    }

    @RequestMapping("/loadReplicationControllerFromYaml")
    public String loadReplicationControllerFromYaml(String path) throws FileNotFoundException {

        ReplicationController aReplicationController = replicationControllersService.loadReplicationControllerFromYaml(path);

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "加载 ReplicationController 成功");
        result.put("data", aReplicationController);

        return JSON.toJSONString(result);
    }

    @RequestMapping("/createReplicationControllerFromYaml")
    public String createReplicationControllerFromYaml(String path) throws FileNotFoundException {

        ReplicationController aReplicationController = replicationControllersService.createReplicationControllerByYaml(path);

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "创建 ReplicationController 成功");
        result.put("data", aReplicationController);

        return JSON.toJSONString(result);
    }

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

        PodsServiceImpl podsService = new PodsServiceImpl();
        ServicesServiceImpl servicesService = new ServicesServiceImpl();
        ReplicationController replicationController = replicationControllersService.getReplicationControllerByNameAndNamespace(name, namespace);
        String uid = replicationController.getMetadata().getUid();
        Map<String, String> matchLabel = replicationController.getSpec().getSelector();
        List<Pod> pods = FilterPodsByControllerUid.filterPodsByControllerUid(uid, podsService.findPodsByLabels(matchLabel));
        Map<String, Object> result = new HashMap<>();
        List<Service> services = servicesService.getServicesByLabels(matchLabel);

        result.put("code", 1200);
        result.put("message", "获取 ReplicaSet Resources 成功");
        result.put("dataReplicationController", replicationController);
        result.put("dataPods", PodFormat.formatPodList(pods));
        result.put("dataServices", services);

        return JSON.toJSONString(result);
    }
}

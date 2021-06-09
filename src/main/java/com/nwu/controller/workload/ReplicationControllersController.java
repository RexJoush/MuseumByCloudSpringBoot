package com.nwu.controller.workload;

/**
 * @author Rex Joush
 * @time 2021.03.22
 */

import com.alibaba.fastjson.JSON;
import com.nwu.service.workload.impl.PodsServiceImpl;
import com.nwu.service.workload.impl.ReplicationControllersServiceImpl;
import com.nwu.util.DealYamlStringFromFront;
import com.nwu.util.format.ReplicationControllerFormat;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.ReplicationController;
import io.kubernetes.client.openapi.ApiException;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Replication Controllers 的 controller 层
 */

/**
 * 修改
 * setReplicas
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
        if(aReplicationController != null) {
            result.put("code", 1200);
            result.put("message", "创建 ReplicationController 成功");
        }else {
            result.put("code", 1299);
            result.put("message", "创建 ReplicationController 失败");
        }

        return JSON.toJSONString(result);
    }

    //删
    @RequestMapping("/deleteReplicationControllerByNameAndNamespace")
    public String deleteReplicationControllerByNameAndNamespace(String name, String namespace){

        Pair<Integer, Boolean> pair = replicationControllersService.deleteReplicationControllerByNameAndNamespace(name, namespace);

        Map<String, Object> result = new HashMap<>();

        result.put("code", pair.getLeft());
        if(pair.getLeft() == 1200) {
            result.put("message", "删除 ReplicationController 成功");
        }else {
            result.put("message", "删除 ReplicationController 失败");
        }
        result.put("data", pair.getRight());

        return JSON.toJSONString(result);
    }

    //改
    @RequestMapping("/changeReplicationControllerByYamlString")
    public String changeReplicationControllerByYamlString(@RequestBody String yaml) throws IOException {

        Map<String, Object> result = new HashMap<>();

        yaml = DealYamlStringFromFront.dealYamlStringFromFront(yaml);
        Pair<Integer, Boolean> pair = replicationControllersService.createOrReplaceReplicationControllerByYamlString(yaml);

        result.put("code", pair.getLeft());
        if(pair.getLeft() == 1200) result.put("message", "修改成功");
        else result.put("message", "修改失败");
        result.put("data", pair.getRight());

        return JSON.toJSONString(result);
    }
    @RequestMapping("/setReplicas")
    public String setReplicas(String name, String namespace, Integer replicas){

        Pair<Integer, Boolean> pair = replicationControllersService.setReplicas(name, namespace, replicas);

        Map<String, Object> result = new HashMap<>();

        result.put("code", pair.getLeft());
        if(pair.getLeft() == 1200) {
            result.put("message", "创建或更新 ReplicaSet 成功");
        }else {
            result.put("message", "创建或更新 ReplicaSet 失败");
        }result.put("data", pair.getRight());

        return JSON.toJSONString(result);
    }

    //查
    @RequestMapping("/getAllReplicationControllers")
    public String findAllReplicationControllers(String namespace) throws ApiException {

        Pair<Integer, List<ReplicationController>> pair;

        if("".equals(namespace)){
            pair = replicationControllersService.findAllReplicationControllers();
        }else {
            pair = replicationControllersService.findReplicationControllersByNamespace(namespace);
        }

        Map<String, Object> result = new HashMap<>();

        result.put("code", pair.getLeft());
        if(pair.getLeft() == 1200) {
            result.put("message", "获取 ReplicationController 列表成功");
            result.put("data", ReplicationControllerFormat.formatReplicationControllerList(pair.getRight()));
        }else {
            result.put("message", "获取 ReplicationController 列表失败");
            result.put("data", null);
        }

        return JSON.toJSONString(result);
    }
    @RequestMapping("/getReplicationControllersByNamespace")
    public String findReplicationControllersByNamespace(String namespace) throws ApiException {

        Pair<Integer, List<ReplicationController>> pair = replicationControllersService.findReplicationControllersByNamespace(namespace);

        Map<String, Object> result = new HashMap<>();

        result.put("code", pair.getLeft());
        if(pair.getLeft() == 1200) {
            result.put("message", "获取 ReplicationController 列表成功");
        }else {
            result.put("message", "获取 ReplicationController 列表失败");
        }
        result.put("data", pair.getRight());

        return JSON.toJSONString(result);
    }
    @RequestMapping("/getReplicationControllerYamlByNameAndNamespace")
    public String getReplicationControllerYamlByNameAndNamespace(String name, String namespace){

        Pair<Integer, String> pair = replicationControllersService.getReplicationControllerYamlByNameAndNamespace(name, namespace);

        Map<String, Object> result = new HashMap<>();

        result.put("code", pair.getLeft());
        if(pair.getLeft() == 1200) {
            result.put("message", "获取 ReplicationController Yaml 成功");
        }else {
            result.put("message", "获取 ReplicationController Yaml 失败");
        }
        result.put("data", pair.getRight());

        return JSON.toJSONString(result);
    }
    @RequestMapping("/getReplicationControllerResources")
    public String getReplicationControllerResources(String name, String namespace){

        Pair<Integer, Map> pair = replicationControllersService.getReplicationControllerResources(name, namespace);

        Map<String, Object> result = new HashMap<>();

        result.put("code", pair.getLeft());
        if(pair.getLeft() == 1200){
            result.put("message", "获取成功");
        }else if(pair.getLeft() == 1201){
            result.put("message", "获取失败");
        } else if(pair.getLeft() == 1202){
            result.put("message", "您的操作有误");
        }else{
            result.put("message", "获取到部分资源");
        }
        result.put("data", pair.getRight());

        return JSON.toJSONString(result);
    }
    @RequestMapping("/getReplicationControllerLogs")
    public String getReplicationControllerLogs(String name ,String namespace){
        //问题，刚开始启动集群时，前端出现timeout
        //未启动的Pod获取不到Log
        //获取 ReplicationController 包含的 Pods
        Pair<Integer, List<Pod>> pair = replicationControllersService.getPodReplicationControllerInvolved(name, namespace);

        Map<String, Object> result = new HashMap<>();
        if(pair.getLeft() == 1200) {
            // 获取每个 Pod 的所有 Logs
            Map<String, Map<String, String>> podLogs = new HashMap<>();
            List<Pod> pods = pair.getRight();
            PodsServiceImpl podsService = new PodsServiceImpl();
            for(int i = 0; i < pods.size(); i++){
                podLogs.put(pods.get(i).getMetadata().getName(), podsService.getPodAllLogs(pods.get(i).getMetadata().getName(), namespace));
            }
            result.put("code", 1200);
            result.put("message", "获取 ReplicationController 日志成功");
            result.put("data", podLogs);
        }else {
            result.put("code", 1201);
            result.put("message", "获取 ReplicationController 日志失败");
            result.put("data", null);
        }

        return JSON.toJSONString(result);
    }
}

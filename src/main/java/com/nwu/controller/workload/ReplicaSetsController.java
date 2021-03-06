package com.nwu.controller.workload;

/**
 * @author Rex Joush
 * @time 2021.03.22
 */

import com.alibaba.fastjson.JSON;
import com.nwu.service.workload.impl.PodsServiceImpl;
import com.nwu.service.workload.impl.ReplicaSetsServiceImpl;
import com.nwu.util.DealYamlStringFromFront;
import com.nwu.util.format.ReplicaSetFormat;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.apps.ReplicaSet;
import io.kubernetes.client.openapi.ApiException;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Replica Sets 的 controller 层
 */

/**
 * 修改
 * setReplicas
 */
@RestController
@RequestMapping("/replicaSets")
public class ReplicaSetsController {
    @Resource
    private ReplicaSetsServiceImpl replicaSetsService;

    //增
    @RequestMapping("/createReplicaSetFromForm")
    public String createReplicaSetFromForm(){
        return "";
    }

    //删
    @RequestMapping("/deleteReplicaSetByNameAndNamespace")
    public String deleteReplicaSetByNameAndNamespace(String name, String namespace){

        Pair<Integer, Boolean> pair = replicaSetsService.deleteReplicaSetByNameAndNamespace(name, namespace);

        Map<String, Object> result = new HashMap<>();

        result.put("code", pair.getLeft());
        if(pair.getLeft() ==1200) {
            result.put("message", "删除 ReplicaSet 成功");
        }else {
            result.put("message", "删除 ReplicaSet 失败");
        }
        result.put("data", pair.getRight());

        return JSON.toJSONString(result);
    }

    //改
    @RequestMapping("/changeReplicaSetByYamlString")
    public String changeReplicaSetByYamlString(@RequestBody String yaml) throws IOException {

        Map<String, Object> result = new HashMap<>();

        yaml = DealYamlStringFromFront.dealYamlStringFromFront(yaml);
        Pair<Integer, Boolean> pair = replicaSetsService.createOrReplaceReplicaSetByYamlString(yaml);

        result.put("code", pair.getLeft());
        if(pair.getLeft() == 1200) result.put("message", "创建成功");
        else result.put("message", "创建失败");
        result.put("data", pair.getRight());

        return JSON.toJSONString(result);
    }
    @RequestMapping("/setReplicas")
    public String setReplicas(String name, String namespace, Integer replicas){

        Pair<Integer, Boolean> pair = replicaSetsService.setReplicas(name, namespace, replicas);

        Map<String, Object> result = new HashMap<>();

        result.put("code", pair.getLeft());
        if(pair.getLeft() == 1200) result.put("message", "更新成功");
        else result.put("message", "更新失败");
        result.put("data", pair.getRight());

        return JSON.toJSONString(result);
    }

    //查
    @RequestMapping("/getAllReplicaSets")
    public String findAllReplicaSets(String namespace) throws ApiException {

        Pair<Integer, List<ReplicaSet>> pair;

        if("".equals(namespace)){
            pair = replicaSetsService.findAllReplicaSets();
        }else {
            pair = replicaSetsService.findReplicaSetsByNamespace(namespace);
        }

        Map<String, Object> result = new HashMap<>();

        result.put("code", pair.getLeft());
        if(pair.getLeft() == 1200) {
            result.put("message", "获取 ReplicaSet 列表成功");
            result.put("data", ReplicaSetFormat.formatReplicaSetList(pair.getRight()));
        }else {
            result.put("message", "获取 ReplicaSet 列表失败");
            result.put("data", null);
        }

        return JSON.toJSONString(result);
    }
    @RequestMapping("/getReplicaSetsByNamespace")
    public String findReplicaSetsByNamespace(String namespace) throws ApiException {

        Pair<Integer, List<ReplicaSet>> pair = replicaSetsService.findReplicaSetsByNamespace(namespace);

        Map<String, Object> result = new HashMap<>();

        result.put("code", pair.getLeft());
        if(pair.getLeft() == 1200) {
            result.put("message", "获取 ReplicaSet 列表成功");
        }else {
            result.put("message", "获取 ReplicaSet 列表失败");
        }
        result.put("data", pair.getRight());

        return JSON.toJSONString(result);
    }
    @RequestMapping("/getReplicaSetByNameAndNamespace")
    public String getReplicaSetByNameAndNamespace(String name, String namespace){

        Pair<Integer, ReplicaSet> pair = replicaSetsService.getReplicaSetByNameAndNamespace(name, namespace);

        Map<String, Object> result = new HashMap<>();

        result.put("code", pair.getLeft());
        if(pair.getLeft() == 1200) {
            result.put("message", "获取 ReplicaSet 成功");
        }else {
            result.put("message", "获取 ReplicaSet 失败");
        }
        result.put("data", pair.getRight());

        return JSON.toJSONString(result);
    }
    @RequestMapping("/getReplicaSetYamlByNameAndNamespace")
    public String getReplicaSetYamlByNameAndNamespace(String name, String namespace){

        Pair<Integer, String> pair = replicaSetsService.getReplicaSetYamlByNameAndNamespace(name, namespace);

        Map<String, Object> result = new HashMap<>();

        result.put("code", pair.getLeft());
        if(pair.getLeft() == 1200) {
            result.put("message", "获取 ReplicaSet Yaml 成功");
        }else {
            result.put("message", "获取 ReplicaSet Yaml 失败");
        }
        result.put("data", pair.getRight());

        return JSON.toJSONString(result);
    }
    @RequestMapping("/getReplicaSetResources")
    public String getReplicaSetResources(String name,String namespace){

        Pair<Integer, Map> pair = replicaSetsService.getReplicaSetResources(name, namespace);

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
    @RequestMapping("/getReplicaSetLogs")
    public String getReplicaSetLogs(String name ,String namespace){

        //获取 ReplicaSet 包含的 Pods
        Pair<Integer, List<Pod>> pair = replicaSetsService.getPodReplicaSetInvolved(name, namespace);

        Map<String, Object> result = new HashMap<>();

        if(pair.getLeft() == 1200) {
            // code = 1202 没有 Pod 时获取 Pod 的日志
            if(pair.getRight() == null){
                result.put("code", 1202);
                result.put("message", "您的操作有误");
                result.put("data", null);
            }
            else{

                PodsServiceImpl podsService = new PodsServiceImpl();
                Pair<Integer, Map> allPodsAllLogs = podsService.getAllPodsAllLogs(pair.getRight());

                result.put("code", allPodsAllLogs.getLeft());
                if(allPodsAllLogs.getLeft() == 1200) result.put("message", "获取 Job 日志成功");
                else if(allPodsAllLogs.getLeft() == 1201) result.put("message", "获取 Job 日志失败");
                else result.put("message", "获取到 Job 部分日志");// code = 1203
                result.put("data", allPodsAllLogs.getRight());
            }
        }else {
            result.put("code", 1201);
            result.put("message", "获取 Job 日志失败");
            result.put("data", null);
        }

        return JSON.toJSONString(result);
    }
}

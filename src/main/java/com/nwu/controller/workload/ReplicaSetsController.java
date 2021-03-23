package com.nwu.controller.workload;

/**
 * @author Rex Joush
 * @time 2021.03.22
 */

import com.alibaba.fastjson.JSON;
import com.nwu.service.workload.impl.CronJobsServiceImpl;
import com.nwu.service.workload.impl.ReplicaSetsServiceImpl;
import io.fabric8.kubernetes.api.model.apps.ReplicaSet;
import io.fabric8.kubernetes.api.model.batch.CronJob;
import io.kubernetes.client.openapi.ApiException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.InputStream;
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

    @RequestMapping("getAllReplicaSets")
    public String findAllReplicaSets() throws ApiException {

        List<ReplicaSet> replicaSetList = replicaSetsService.findAllReplicaSets();

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "获取 ReplicaSet 列表成功");
        result.put("data", replicaSetList);

        return JSON.toJSONString(result);

    }

    @RequestMapping("getReplicaSetsByNamespace")
    public String findReplicaSetsByNamespace(String namespace) throws ApiException {

        List<ReplicaSet> replicaSetList = replicaSetsService.findReplicaSetsByNamespace(namespace);

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "获取 ReplicaSet 列表成功");
        result.put("data", replicaSetList);

        return JSON.toJSONString(result);

    }

    @RequestMapping("deleteReplicaSetByNameAndNamespace")
    public String deleteReplicaSetByNameAndNamespace(String name, String namespace){
        Boolean delete = replicaSetsService.deleteReplicaSetByNameAndNamespace(name, namespace);

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "删除 ReplicaSet 成功");
        result.put("data", delete);

        return JSON.toJSONString(result);
    }

    @RequestMapping("loadReplicaSetFromYaml")
    public String loadReplicaSetFromYaml(InputStream yamlInputStream){

        ReplicaSet aReplicaSet = replicaSetsService.loadReplicaSetFromYaml(yamlInputStream);

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "加载 ReplicaSet 成功");
        result.put("data", aReplicaSet);

        return JSON.toJSONString(result);
    }

    @RequestMapping("loadReplicaSetFromYaml")
    public String createReplicaSetFromYaml(InputStream yamlInputStream){

        ReplicaSet aReplicaSet = replicaSetsService.createReplicaSetByYaml(yamlInputStream);

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "创建 ReplicaSet 成功");
        result.put("data", aReplicaSet);

        return JSON.toJSONString(result);
    }

    @RequestMapping("/createOrReplaceReplicaSet")
    public String createOrReplaceReplicaSet(InputStream yamlInputStream){
        ReplicaSet aReplicaSet = replicaSetsService.createOrReplaceReplicaSet(yamlInputStream);

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "创建或更新 ReplicaSet 成功");
        result.put("data", aReplicaSet);

        return JSON.toJSONString(result);
    }

}

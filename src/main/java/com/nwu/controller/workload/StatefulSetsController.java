package com.nwu.controller.workload;

/**
 * @author Rex Joush
 * @time 2021.03.22
 */

import com.alibaba.fastjson.JSON;
import com.nwu.service.workload.impl.PodsServiceImpl;
import com.nwu.service.workload.impl.StatefulSetsServiceImpl;
import com.nwu.util.DealYamlStringFromFront;
import com.nwu.util.format.StatefulSetFormat;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.apps.StatefulSet;
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
 * Stateful Sets 的 controller 层
 */
@RestController
@RequestMapping("/statefulSets")
public class StatefulSetsController {

    @Resource
    private StatefulSetsServiceImpl statefulSetsService;

    //增
    @RequestMapping("/createStatefulSetFromForm")
    public String createStatefulSetFromForm(){
        return "";
    }

    //删
    @RequestMapping("/deleteStatefulSetByNameAndNamespace")
    public String deleteStatefulSetByNameAndNamespace(String name, String namespace){

        Pair<Integer, Boolean> pair = statefulSetsService.deleteStatefulSetByNameAndNamespace(name, namespace);

        Map<String, Object> result = new HashMap<>();

        result.put("code", pair.getLeft());
        if(pair.getLeft() == 1200) {
            result.put("message", "删除 StatefulSet 成功");
        }else {
            result.put("message", "删除 StatefulSet 失败");
        }
        result.put("data", pair.getRight());

        return JSON.toJSONString(result);
    }

    //改
    @RequestMapping("/changeStatefulSetByYamlString")
    public String changeStatefulSetByYamlString(@RequestBody String yaml) throws IOException {

        Map<String, Object> result = new HashMap<>();

        yaml = DealYamlStringFromFront.dealYamlStringFromFront(yaml);
        Pair<Integer, Boolean> pair = statefulSetsService.createOrReplaceStatefulSetByYamlString(yaml);

        result.put("code", pair.getLeft());
        if(pair.getLeft() == 1200) result.put("message", "创建成功");
        else result.put("message", "创建失败");
        result.put("data", pair.getRight());

        return JSON.toJSONString(result);
    }

    //查
    @RequestMapping("/getAllStatefulSets")
    public String findAllStatefulSets(String namespace) throws ApiException {

        Pair<Integer, List<StatefulSet>> pair;

        if("".equals(namespace)){
            pair = statefulSetsService.findAllStatefulSets();
        }else {
            pair = statefulSetsService.findStatefulSetsByNamespace(namespace);
        }

        Map<String, Object> result = new HashMap<>();

        result.put("code", pair.getLeft());
        if(pair.getLeft() == 1200) {
            result.put("message", "获取 StatefulSet 列表成功");
            result.put("data", StatefulSetFormat.formatStatefulSetList(pair.getRight()));

        }else {
            result.put("message", "获取 StatefulSet 列表失败");
            result.put("data", null);
        }

        return JSON.toJSONString(result);
    }
    @RequestMapping("/getStatefulSetsByNamespace")
    public String findStatefulSetsByNamespace(String namespace) throws ApiException {

        Pair<Integer, List<StatefulSet>> pair = statefulSetsService.findStatefulSetsByNamespace(namespace);

        Map<String, Object> result = new HashMap<>();

        result.put("code", pair.getLeft());
        if(pair.getLeft() == 1200) {
            result.put("message", "获取 StatefulSet 列表成功");
        }else {
            result.put("message", "获取 StatefulSet 列表失败");
        }
        result.put("data", pair.getRight());

        return JSON.toJSONString(result);
    }
    @RequestMapping("/getStatefulSetYamlByNameAndNamespace")
    public String getStatefulSetYamlByNameAndNamespace(String name, String namespace){

        Pair<Integer, String> pair = statefulSetsService.getStatefulSetYamlByNameAndNamespace(name, namespace);

        Map<String, Object> result = new HashMap<>();

        result.put("code", pair.getLeft());
        if(pair.getLeft() == 1200) {
            result.put("message", "获取 StatefulSet Yaml 成功");
        }else {
            result.put("message", "获取 StatefulSet Yaml 失败");
        }
        result.put("data", pair.getRight());

        return JSON.toJSONString(result);
    }
    @RequestMapping("/getStatefulSetResources")
    public String getStatefulSetResources(String name,String namespace){

        Pair<Integer, Map> pair = statefulSetsService.getStatefulSetResources(name, namespace);

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
    @RequestMapping("/getStatefulSetLogs")
    public String getStatefulSetLogs(String name ,String namespace){

        //获取 StatefulSet 包含的 Pods
        Pair<Integer, List<Pod>> pair = statefulSetsService.getPodStatefulSetInvolved(name, namespace);

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

package com.nwu.controller.workload;

/**
 * @author Rex Joush
 * @time 2021.03.22
 */

import com.alibaba.fastjson.JSON;
import com.nwu.service.workload.impl.DaemonSetsServiceImpl;
import com.nwu.service.workload.impl.PodsServiceImpl;
import com.nwu.util.DealYamlStringFromFront;
import com.nwu.util.format.DaemonSetFormat;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.apps.DaemonSet;
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
 * Daemon Sets 的 controller 层
 */
@RestController
@RequestMapping("/daemonSets")
public class DaemonSetsController {
    @Resource
    private DaemonSetsServiceImpl daemonSetsService;

    //增
    @RequestMapping("/createOrReplaceDaemonSetByYaml")
    public String createOrReplaceDaemonSetByYaml(String yaml) throws IOException {

        Boolean ok = daemonSetsService.createOrReplaceDaemonSetByYaml(yaml);

        Map<String, Object> result = new HashMap<>();
        if(ok != null) {
            result.put("code", 1200);
            result.put("message", ok ? "创建或更新 DaemonSet 成功" : "创建或更新 DaemonSet 失败");
        }else {
            result.put("code", 1299);
            result.put("message", "创建或更新 DaemonSet 失败");
        }

        return JSON.toJSONString(result);
    }

    //删
    @RequestMapping("/deleteDaemonSetByNameAndNamespace")
    public String deleteDaemonSetByNameAndNamespace(String name, String namespace) throws ApiException {

        Pair<Integer, Boolean> pair = daemonSetsService.deleteDaemonSetByNameAndNamespace(name, namespace);

        Map<String, Object> result = new HashMap<>();
        result.put("code", pair.getLeft());
        if(pair.getLeft() == 1200 ) {
            result.put("message", "删除 DaemonSet 成功");
        }else {
            result.put("message", "删除 DaemonSet 失败");
        }
        result.put("data", pair.getRight());

        return JSON.toJSONString(result);
    }

    //改
    @RequestMapping("/changeDaemonSetByYamlString")
    public String changeDaemonSetByYamlString(@RequestBody String yaml) throws IOException {

        Map<String, Object> result = new HashMap<>();

        yaml = DealYamlStringFromFront.dealYamlStringFromFront(yaml);
        Pair<Integer, Boolean> pair = daemonSetsService.createOrReplaceDaemonSetByYamlString(yaml);

        result.put("code", pair.getLeft());
        if(pair.getLeft() == 1200) result.put("message", "创建成功");
        else result.put("message", "创建失败");
        result.put("data", pair.getRight());

        return JSON.toJSONString(result);
    }

    //查
    @RequestMapping("/getAllDaemonSets")
    public String findAllDaemonSets(String namespace) throws ApiException {

        System.out.println(namespace);

        Pair<Integer, List<DaemonSet>> pair;

        if("".equals(namespace)){
            pair = daemonSetsService.findAllDaemonSets();
        }else{
            pair = daemonSetsService.findDaemonSetsByNamespace(namespace);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("code", pair.getLeft());
        if(pair.getLeft() == 1200) {
            result.put("message", "获取 DaemonSet 列表成功");
            result.put("data", DaemonSetFormat.formatDaemonSetList(pair.getRight()));
        }else {
            result.put("message", "获取 DaemonSet 列表失败");
            result.put("data", null);
        }

        return JSON.toJSONString(result);
    }
    @RequestMapping("/getDaemonSetsByNamespace")
    public String findDaemonSetsByNamespace(String namespace) throws ApiException {

        Pair<Integer, List<DaemonSet>> pair = daemonSetsService.findDaemonSetsByNamespace(namespace);

        Map<String, Object> result = new HashMap<>();
        result.put("code", pair.getLeft());
        if(pair.getLeft() == 1200) {
            result.put("message", "获取 DaemonSet 列表成功");
        }else {
            result.put("message", "获取 DaemonSet 列表失败");
        }
        result.put("data", pair.getRight());

        return JSON.toJSONString(result);
    }
    @RequestMapping("/getDaemonSetByNameAndNamespace")
    public String getDaemonSetByNameAndNamespace(String name, String namespace) throws ApiException {

        Pair<Integer, DaemonSet> pair = daemonSetsService.getDaemonSetByNameAndNamespace(name, namespace);

        Map<String, Object> result = new HashMap<>();

        result.put("code", pair.getLeft());
        if(pair.getLeft() == 1200) {
            result.put("message", "获取 DaemonSet 成功");
        }else {
            result.put("message", "获取 DaemonSet 失败");
        }
        result.put("data", pair.getRight());


        return JSON.toJSONString(result);
    }
    @RequestMapping("/getDaemonSetYamlByNameAndNamespace")
    public String getDaemonSetYamlByNameAndNamespace(String name, String namespace){

        Pair<Integer, String> pair = daemonSetsService.getDaemonSetYamlByNameAndNamespace(name, namespace);

        Map<String, Object> result = new HashMap<>();

        result.put("code", pair.getLeft());
        if(pair.getLeft() == 1200) {
            result.put("message", "获取 DaemonSet Yaml 成功");
        }else {
            result.put("message", "获取 DaemonSet Yaml 失败");
        }
        result.put("data", pair.getRight());

        return JSON.toJSONString(result);
    }
    @RequestMapping("/getDaemonSetLogs")
    public String getDaemonSetLogs(String name ,String namespace){

        Map<String, Object> result = new HashMap<>();

        //获取 DaemonSet 包含的 Pods
        Pair<Integer, List<Pod>> pair = daemonSetsService.getPodDaemonSetInvolved(name, namespace);

        result.put("code", pair.getLeft());
        if(pair.getLeft() == 1200) {
            // 获取每个 Pod 的所有 Logs
            List<Pod> pods = pair.getRight();
            Map<String, Map<String, String>> podLogs = new HashMap<>();
            PodsServiceImpl podsService = new PodsServiceImpl();
            for(int i = 0; i < pods.size(); i++){
                //此处假设 Pod 获取成功之后相应的 Log 也能获取成功
                podLogs.put(pods.get(i).getMetadata().getName(), podsService.getPodAllLogs(pods.get(i).getMetadata().getName(), namespace));
            }
            result.put("message", "获取 DaemonSet 日志成功");
            result.put("data", podLogs);
        }else {
            result.put("message", "获取 DaemonSet 日志失败");
            result.put("data", null);
        }

        return JSON.toJSONString(result);
    }
    @RequestMapping("/getDaemonSetResources")
    public String getDaemonSetResources(String name, String namespace){

        //获取 DaemonSet
        Pair<Integer, Map> pair = daemonSetsService.getDaemonSetResources(name, namespace);

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
}

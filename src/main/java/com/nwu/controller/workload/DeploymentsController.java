package com.nwu.controller.workload;

/**
 * @author Rex Joush
 * @time 2021.03.22
 */

import com.alibaba.fastjson.JSON;
import com.nwu.service.workload.impl.DeploymentsServiceImpl;
import com.nwu.util.DealYamlStringFromFront;
import com.nwu.util.format.DeploymentFormat;
import io.fabric8.kubernetes.api.model.apps.Deployment;
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
 * Deployments 的 controller 层
 */

/** 需要修改
 * changeDeploymentByYaml 方法
 * setReplica
 */
@RestController
@RequestMapping("/deployments")
public class DeploymentsController {
    @Resource
    private DeploymentsServiceImpl deploymentsService;

    //增

    //删
    @RequestMapping("/deleteDeploymentByNameAndNamespace")
    public String deleteDeploymentByNameAndNamespace(String name, String namespace){

        Pair<Integer, Boolean> pair = deploymentsService.deleteDeploymentByNameAndNamespace(name, namespace);

        Map<String, Object> result = new HashMap<>();

        result.put("code", pair.getLeft());
        if(pair.getLeft() == 1200) {
            result.put("message", "删除 Deployment 成功");
        }else {
            result.put("message", "删除 Deployment 失败");
        }
        result.put("data", pair.getRight());

        return JSON.toJSONString(result);
    }

    //改
    @RequestMapping("/changeDeploymentByYamlString")
    public String changeDeploymentByYaml(@RequestBody String yaml) throws IOException {

        yaml = DealYamlStringFromFront.dealYamlStringFromFront(yaml);
        Pair<Integer, Boolean> pair = deploymentsService.createOrReplaceDeploymentByYamlString(yaml);

        Map<String, Object> result = new HashMap<>();

        result.put("code", pair.getLeft());
        if(pair.getLeft() == 1200) result.put("message", "创建成功");
        else result.put("message", "创建失败");
        result.put("data", pair.getRight());

        return JSON.toJSONString(result);
    }
    @RequestMapping("/createOrReplaceDeploymentByPath")
    public String createOrReplaceDeploymentByPath(String path) throws FileNotFoundException {

        Deployment aDeployment = deploymentsService.createOrReplaceDeploymentByPath(path);

        Map<String, Object> result = new HashMap<>();

        if(aDeployment != null) {
            result.put("code", 1200);
            result.put("message", "创建 Deployment 成功");
        }else {
            result.put("code", 1299);
            result.put("message", "创建 Deployment 失败");
        }

        return JSON.toJSONString(result);
    }
    @RequestMapping("/setReplica")
    public String setReplica(String name, String namespace, String replica){

        Pair<Integer, Boolean> pair = deploymentsService.setReplicas(name, namespace, Integer.parseInt(replica));

        Map<String, Object> result = new HashMap<>();

        result.put("code", pair.getLeft());
        if(pair.getLeft() == 1200) {
            result.put("message", "设置 Deployment的replica 成功");
        }else {
            result.put("message", "设置 Deployment的replica 失败");
        }
        result.put("data", pair.getRight());

        return JSON.toJSONString(result);
    }

    //查
    @RequestMapping("/getAllDeployments")
    public String findAllDeployments(String namespace) throws ApiException {

        Pair<Integer, List<Deployment>> pair;

        if("".equals(namespace)){
            pair = deploymentsService.findAllDeployments();
        }else{
            pair = deploymentsService.findDeploymentsByNamespace(namespace);
        }

        Map<String, Object> result = new HashMap<>();

        result.put("code", pair.getLeft());
        if(pair.getLeft() == 1200) {
            result.put("message", "获取 Deployment 列表成功");
            result.put("data", DeploymentFormat.formatDeploymentList(pair.getRight()));
        }else {
            result.put("message", "获取 Deployment 列表失败");
            result.put("data", null);
        }

        return JSON.toJSONString(result);
    }
    @RequestMapping("/getDeploymentsByNamespace")
    public String findDeploymentsByNamespace(String namespace) throws ApiException {

        Pair<Integer, List<Deployment>> pair = deploymentsService.findDeploymentsByNamespace(namespace);

        Map<String, Object> result = new HashMap<>();

        result.put("code", pair.getLeft());
        if(pair.getLeft() == 1200) {
            result.put("message", "获取 Deployment 列表成功");
        }else {
            result.put("message", "获取 Deployment 列表失败");
        }
        result.put("data", pair.getRight());

        return JSON.toJSONString(result);
    }
    @RequestMapping("/getDeploymentByNameAndNamespace")
    public String getDeploymentByNameAndNamespace(String name, String namespace){

        Pair<Integer, Deployment> pair = deploymentsService.getDeploymentByNameAndNamespace(name, namespace);

        Map<String, Object> result = new HashMap<>();

        result.put("code", pair.getLeft());
        if(pair.getLeft() == 1200) {
            result.put("message", "获取 Deployment 成功");
        }else {
            result.put("message", "获取 Deployment 失败功");
        }
        result.put("data", pair.getRight());

        return JSON.toJSONString(result);
    }
    @RequestMapping("/getDeploymentYamlByNameAndNamespace")
    public String getDeploymentYamlByNameAndNamespace(String name, String namespace) throws ApiException {

        Pair<Integer, String> pair = deploymentsService.getDeploymentYamlByNameAndNamespace(name, namespace);

        Map<String, Object> result = new HashMap<>();

        result.put("code", pair.getLeft());
        if(pair.getLeft() == 1200) {
            result.put("message", "获取 Deployment Yaml 成功");
        }else {
            result.put("message", "获取 Deployment Yaml 失败");
        }
        result.put("data", pair.getRight());

        return JSON.toJSONString(result);
    }
    @RequestMapping("/getDeploymentResources")
    public String getDeploymentResources(String name, String namespace){

        Pair<Integer, Map> pair = deploymentsService.getDeploymentResources(name, namespace);

        // 返回数据
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

    //弃用
//    @RequestMapping("/getDeploymentLogByNameAndNamespace")
//    public String getDeploymentLogByNameAndNamespace(String name, String namespace){
//        String str = deploymentsService.getDeploymentLogByNameAndNamespace(name, namespace);
//
//        Map<String, Object> result = new HashMap<>();
//
//        result.put("code", 1200);
//        result.put("message", "获取 Deployment日志 成功");
//        result.put("data", str);
//
//        return JSON.toJSONString(result);
//    }
}

package com.nwu.controller.workload;

/**
 * @author Rex Joush
 * @time 2021.03.22
 */

import com.alibaba.fastjson.JSON;
import com.nwu.service.workload.impl.DaemonSetsServiceImpl;
import com.nwu.service.workload.impl.DeploymentsServiceImpl;
import io.fabric8.kubernetes.api.model.apps.DaemonSet;
import io.fabric8.kubernetes.api.model.apps.Deployment;
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
 * Deployments 的 controller 层
 */
@RestController
@RequestMapping("/deployments")
public class DeploymentsController {
    @Resource
    private DeploymentsServiceImpl deploymentsService;

    @RequestMapping("/getAllDeployments")
    public String findAllDeployments() throws ApiException {

        List<Deployment> deployments = deploymentsService.findAllDeployments();

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "获取 Deployment 列表成功");
        result.put("data", deployments);

        return JSON.toJSONString(result);

    }

    @RequestMapping("/getDeploymentsByNamespace")
    public String findDeploymentsByNamespace(String namespace) throws ApiException {

        List<Deployment> v1DeploymentList = deploymentsService.findDeploymentsByNamespace(namespace);

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "获取 Deployment 列表成功");
        result.put("data", v1DeploymentList);

        return JSON.toJSONString(result);
    }

    @RequestMapping("/getDeploymentByNameAndNamespace")
    public String getDeploymentByNameAndNamespace(String name, String namespace){

        Deployment deployment = deploymentsService.getDeploymentByNameAndNamespace(name, namespace);

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "通过 name namespace 获取 Deployment 成功");
        result.put("data", deployment);

        return JSON.toJSONString(result);
    }

    @RequestMapping("/deleteDeploymentByNameAndNamespace")
    public String deleteDeploymentByNameAndNamespace(String name, String namespace){
        Boolean delete = deploymentsService.deleteDeploymentByNameAndNamespace(name, namespace);

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "删除 Deployment 成功");
        result.put("data", delete);

        return JSON.toJSONString(result);
    }

    @RequestMapping("/loadDeploymentFromYaml")
    public String loadDeploymentFromYaml(String path) throws FileNotFoundException {

        Deployment aDeployment = deploymentsService.loadDeploymentFromYaml(path);

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "加载 Deployment 成功");
        result.put("data", aDeployment);

        return JSON.toJSONString(result);
    }

    @RequestMapping("/createDeploymentByYaml")
    public String createDeploymentByYaml(String path) throws FileNotFoundException {

        Deployment aDeployment = deploymentsService.createDeploymentByYaml(path);

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "创建 Deployment 成功");
        result.put("data", aDeployment);

        return JSON.toJSONString(result);
    }

    @RequestMapping("/createOrReplaceDeployment")
    public String createOrReplaceDeployment(String path) throws FileNotFoundException {
        Deployment aDeployment = deploymentsService.createOrReplaceDeployment(path);

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "创建或更新 Deployment 成功");
        result.put("data", aDeployment);

        return JSON.toJSONString(result);
    }

    @RequestMapping("/getDeploymentLogByNameAndNamespace")
    public String getDeploymentLogByNameAndNamespace(String name, String namespace){
        String str = deploymentsService.getDeploymentLogByNameAndNamespace(name, namespace);

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "获取 Deployment日志 成功");
        result.put("data", str);

        return JSON.toJSONString(result);
    }

    @RequestMapping("/setReplicas")
    public String setReplicas(String name, String namespace, Integer replicas){

        deploymentsService.setReplicas(name, namespace, replicas);

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "可能设置 Deployment的replicas 成功");
        result.put("data", "未明确");

        return JSON.toJSONString(result);
    }

}

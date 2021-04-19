package com.nwu.controller.workload;

/**
 * @author Rex Joush
 * @time 2021.03.22
 */

import com.alibaba.fastjson.JSON;
import com.nwu.entity.workload.ReplicaSetInformation;
import com.nwu.service.workload.impl.DeploymentsServiceImpl;
import com.nwu.service.workload.impl.ReplicaSetsServiceImpl;
import com.nwu.util.FilterReplicaSetByControllerUid;
import com.nwu.util.KubernetesUtils;
import com.nwu.util.format.ReplicaSetFormat;
import io.fabric8.kubernetes.api.model.apps.Deployment;
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
 * Deployments 的 controller 层
 */
@RestController
@RequestMapping("/deployments")
public class DeploymentsController {
    @Resource
    private DeploymentsServiceImpl deploymentsService;

    @RequestMapping("/getAllDeployments")
    public String findAllDeployments(String namespace) throws ApiException {

        List<Deployment> deployments;

        if("".equals(namespace)){
            deployments = deploymentsService.findAllDeployments();
        }else{
            deployments = deploymentsService.findDeploymentsByNamespace(namespace);
        }

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

    @RequestMapping("/getDeploymentYamlByNameAndNamespace")
    public String getDeploymentYamlByNameAndNamespace(String name, String namespace){

        String deploymentYaml = deploymentsService.getDeploymentYamlByNameAndNamespace(name, namespace);
        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "获取 Deployment Yaml 成功");
        result.put("data", deploymentYaml);

        return JSON.toJSONString(result);
    }

    @RequestMapping("/getDeploymentResources")
    public String getDeploymentResources(String name, String namespace){

        Deployment deployment = deploymentsService.getDeploymentByNameAndNamespace(name, namespace);

        //获取 Deployment 下的 ReplicaSet(新[0]旧[1:-1]) 正确匹配按ControllerUid
        ReplicaSetsServiceImpl replicaSetsService = new ReplicaSetsServiceImpl();
        List<ReplicaSet> replicaSetList = KubernetesUtils.client.apps().replicaSets().inAnyNamespace().list().getItems();
        replicaSetList = FilterReplicaSetByControllerUid.filterReplicaSetsByControllerUid(deployment.getMetadata().getUid(), replicaSetList);
        List<ReplicaSetInformation> replicaSets = ReplicaSetFormat.formatReplicaSetList(replicaSetList);

        //获取 Deployment 下的 ReplicaSet(新[0]旧[1:-1]) 错误匹配按Selector
//        Map<String, String> m = deployment.getSpec().getSelector().getMatchLabels();
//        ReplicaSetsServiceImpl replicaSetsService = new ReplicaSetsServiceImpl();
//        List<ReplicaSet> replicaSetList = KubernetesUtils.client.apps().replicaSets().inAnyNamespace().withLabels(m).list().getItems();
//        List<ReplicaSetInformation> replicaSets = ReplicaSetFormat.formatReplicaSetList(replicaSetList);

        int flag= 0;
        String minimum = replicaSets.get(0).getCreationTimestamp().replace("T", "").replace("Z", "");
        for(int i = 1; i < replicaSets.size(); i ++){
            String tmp = replicaSets.get(i).getCreationTimestamp().replace("T", "").replace("Z", "");
            if(tmp.compareTo(minimum) == -1){
                minimum = tmp;
                flag = i;
            }
        }
        ReplicaSetInformation tmpReplicaSetInformation = new ReplicaSetInformation();
        tmpReplicaSetInformation = replicaSets.get(0);
        replicaSets.set(0, replicaSets.get(flag));
        replicaSets.set(flag, tmpReplicaSetInformation);

        //封装数据
        Map<String, Object> data = new HashMap<>();
        data.put("deployment", deployment);
        data.put("newReplicaSets", replicaSets.subList(0,1));
        data.put("oldReplicaSets", replicaSets.subList(1, replicaSets.size()));

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "获取 Deployment 的 Resources 成功");
        result.put("data", data);

        return JSON.toJSONString(result);
    }
}

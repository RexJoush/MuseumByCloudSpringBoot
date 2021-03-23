package com.nwu.controller.workload;

/**
 * @author Rex Joush
 * @time 2021.03.22
 */

import com.alibaba.fastjson.JSON;
import com.nwu.service.workload.impl.CronJobsServiceImpl;
import com.nwu.service.workload.impl.ReplicationControllersServiceImpl;
import io.fabric8.kubernetes.api.model.ReplicationController;
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
 * Replication Controllers 的 controller 层
 */
@RestController
@RequestMapping("/replicationControllers")
public class ReplicationControllersController {

    @Resource
    private ReplicationControllersServiceImpl replicationControllersService;

    @RequestMapping("getAllReplicationControllers")
    public String findAllReplicationControllers() throws ApiException {

        List<ReplicationController> replicationControllerList = replicationControllersService.findAllReplicationControllers();

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "获取 ReplicationController 列表成功");
        result.put("data", replicationControllerList);

        return JSON.toJSONString(result);

    }

    @RequestMapping("getReplicationControllersByNamespace")
    public String findReplicationControllersByNamespace(String namespace) throws ApiException {

        List<ReplicationController> replicationControllerList = replicationControllersService.findReplicationControllersByNamespace(namespace);

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "获取 ReplicationController 列表成功");
        result.put("data", replicationControllerList);

        return JSON.toJSONString(result);

    }

    @RequestMapping("deleteReplicationControllerByNameAndNamespace")
    public String deleteReplicationControllerByNameAndNamespace(String name, String namespace){
        Boolean delete = replicationControllersService.deleteReplicationControllerByNameAndNamespace(name, namespace);

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "删除 ReplicationController 成功");
        result.put("data", delete);

        return JSON.toJSONString(result);
    }

    @RequestMapping("loadCronJobFromYaml")
    public String loadReplicationControllerFromYaml(InputStream yamlInputStream){

        ReplicationController aReplicationController = replicationControllersService.loadReplicationControllerFromYaml(yamlInputStream);

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "加载 ReplicationController 成功");
        result.put("data", aReplicationController);

        return JSON.toJSONString(result);
    }

    @RequestMapping("createReplicationControllerFromYaml")
    public String createReplicationControllerFromYaml(InputStream yamlInputStream){

        ReplicationController aReplicationController = replicationControllersService.createReplicationControllerByYaml(yamlInputStream);

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "创建 ReplicationController 成功");
        result.put("data", aReplicationController);

        return JSON.toJSONString(result);
    }
}

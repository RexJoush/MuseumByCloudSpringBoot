package com.nwu.controller.workload;

/**
 * @author Rex Joush
 * @time 2021.03.22
 */

import com.alibaba.fastjson.JSON;
import com.nwu.service.workload.impl.CronJobsServiceImpl;
import com.nwu.service.workload.impl.StatefulSetsServiceImpl;
import io.fabric8.kubernetes.api.model.ReplicationController;
import io.fabric8.kubernetes.api.model.apps.StatefulSet;
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
 * Stateful Sets 的 controller 层
 */
@RestController
@RequestMapping("/statefulSets")
public class StatefulSetsController {

    @Resource
    private StatefulSetsServiceImpl statefulSetsService;

    @RequestMapping("/getAllStatefulSets")
    public String findAllStatefulSets() throws ApiException {

        List<StatefulSet> statefulSetList = statefulSetsService.findAllStatefulSets();

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "获取 StatefulSet 列表成功");
        result.put("data", statefulSetList);

        return JSON.toJSONString(result);

    }

    @RequestMapping("/getStatefulSetsByNamespace")
    public String findStatefulSetsByNamespace(String namespace) throws ApiException {

        List<StatefulSet> statefulSetList = statefulSetsService.findStatefulSetsByNamespace(namespace);

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "获取 StatefulSet 列表成功");
        result.put("data", statefulSetList);

        return JSON.toJSONString(result);

    }

    @RequestMapping("/deleteStatefulSetByNameAndNamespace")
    public String deleteStatefulSetByNameAndNamespace(String name, String namespace){
        Boolean delete = statefulSetsService.deleteStatefulSetByNameAndNamespace(name, namespace);

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "删除 StatefulSet 成功");
        result.put("data", delete);

        return JSON.toJSONString(result);
    }

    @RequestMapping("/loadStatefulSetFromYaml")
    public String loadStatefulSetFromYaml(String path) throws FileNotFoundException {

        StatefulSet aStatefulSet = statefulSetsService.loadStatefulSetFromYaml(path);

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "加载 StatefulSet 成功");
        result.put("data", aStatefulSet);

        return JSON.toJSONString(result);
    }

    @RequestMapping("/createStatefulSetFromYaml")
    public String createStatefulSetFromYaml(String path) throws FileNotFoundException {

        StatefulSet aStatefulSet = statefulSetsService.createStatefulSetByYaml(path);

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "创建 StatefulSet 成功");
        result.put("data", aStatefulSet);

        return JSON.toJSONString(result);
    }

    @RequestMapping("/createOrReplaceStatefulSet")
    public String createOrReplaceStatefulSet(String path) throws FileNotFoundException {
        StatefulSet aStatefulSet = statefulSetsService.createOrReplaceStatefulSet(path);

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "创建或更新 StatefulSet 成功");
        result.put("data", aStatefulSet);

        return JSON.toJSONString(result);
    }

}

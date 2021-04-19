package com.nwu.controller.workload;

/**
 * @author Rex Joush
 * @time 2021.03.22
 */

import com.alibaba.fastjson.JSON;
import com.nwu.service.explorebalancing.impl.ServicesServiceImpl;
import com.nwu.service.workload.impl.CronJobsServiceImpl;
import com.nwu.service.workload.impl.PodsServiceImpl;
import com.nwu.service.workload.impl.StatefulSetsServiceImpl;
import com.nwu.util.FilterPodsByControllerUid;
import com.nwu.util.format.PodFormat;
import com.nwu.util.format.StatefulSetFormat;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.ReplicationController;
import io.fabric8.kubernetes.api.model.Service;
import io.fabric8.kubernetes.api.model.apps.ReplicaSet;
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
    public String findAllStatefulSets(String namespace) throws ApiException {

        List<StatefulSet> statefulSetList;

        if("".equals(namespace)){
            statefulSetList = statefulSetsService.findAllStatefulSets();
        }else {
            statefulSetList = statefulSetsService.findStatefulSetsByNamespace(namespace);
        }

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "获取 StatefulSet 列表成功");
        result.put("data", StatefulSetFormat.formatStatefulSetList(statefulSetList));

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

    @RequestMapping("/getStatefulSetYamlByNameAndNamespace")
    public String getStatefulSetYamlByNameAndNamespace(String name, String namespace){

        String statefulSetYaml = statefulSetsService.getStatefulSetYamlByNameAndNamespace(name, namespace);
        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "获取 StatefulSet Yaml 成功");
        result.put("data", statefulSetYaml);

        return JSON.toJSONString(result);
    }

    @RequestMapping("/getStatefulSetResources")
    public String getStatefulSetResources(String name,String namespace){
        System.out.println(name + namespace);

        PodsServiceImpl podsService = new PodsServiceImpl();
        StatefulSet statefulSet = statefulSetsService.getStatefulSetByNameAndNamespace(name, namespace);
        String uid = statefulSet.getMetadata().getUid();
        Map<String, String> matchLabels = statefulSet.getSpec().getSelector().getMatchLabels();
//        System.out.println(matchLabels);
        Map<String, Object> result = new HashMap<>();
        List<Pod> pods = FilterPodsByControllerUid.filterPodsByControllerUid(uid, podsService.findPodsByLabels(matchLabels));


        result.put("code", 1200);
        result.put("message", "获取 StatefulSet Resources 成功");
        result.put("dataStatefulSet", statefulSet);
        result.put("dataPods", PodFormat.formatPodList(pods));

        return JSON.toJSONString(result);
    }
}

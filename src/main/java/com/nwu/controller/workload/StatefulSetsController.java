package com.nwu.controller.workload;

/**
 * @author Rex Joush
 * @time 2021.03.22
 */

import com.alibaba.fastjson.JSON;
import com.nwu.service.impl.CommonServiceImpl;
import com.nwu.service.workload.impl.PodsServiceImpl;
import com.nwu.service.workload.impl.StatefulSetsServiceImpl;
import com.nwu.util.FilterPodsByControllerUid;
import com.nwu.util.format.PodFormat;
import com.nwu.util.format.StatefulSetFormat;
import io.fabric8.kubernetes.api.model.Event;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.apps.StatefulSet;
import io.kubernetes.client.openapi.ApiException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.FileNotFoundException;
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
    @RequestMapping("/createStatefulSetFromYaml")
    public String createStatefulSetFromYaml(String path) throws FileNotFoundException {

        StatefulSet aStatefulSet = statefulSetsService.createStatefulSetByYaml(path);

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "创建 StatefulSet 成功");
        result.put("data", aStatefulSet);

        return JSON.toJSONString(result);
    }

    //删
    @RequestMapping("/deleteStatefulSetByNameAndNamespace")
    public String deleteStatefulSetByNameAndNamespace(String name, String namespace){

        Boolean delete = statefulSetsService.deleteStatefulSetByNameAndNamespace(name, namespace);

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "删除 StatefulSet 成功");
        result.put("data", delete);

        return JSON.toJSONString(result);
    }

    //改
    @RequestMapping("/createOrReplaceStatefulSet")
    public String createOrReplaceStatefulSet(String path) throws FileNotFoundException {

        StatefulSet aStatefulSet = statefulSetsService.createOrReplaceStatefulSet(path);

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "创建或更新 StatefulSet 成功");
        result.put("data", aStatefulSet);

        return JSON.toJSONString(result);
    }

    //查
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

        //获取 StatefulSet
        StatefulSet statefulSet = statefulSetsService.getStatefulSetByNameAndNamespace(name, namespace);
        String uid = statefulSet.getMetadata().getUid();
        Map<String, String> matchLabels = statefulSet.getSpec().getSelector().getMatchLabels();

        //获取 Pods
        PodsServiceImpl podsService = new PodsServiceImpl();
        List<Pod> pods = FilterPodsByControllerUid.filterPodsByControllerUid(uid, podsService.findPodsByLabels(matchLabels));

        //获取事件
        List<Event> events = CommonServiceImpl.getEventByInvolvedObjectUid(statefulSet.getMetadata().getUid());

        //封装数据
        Map<String, Object> data = new HashMap<>();
        data.put("statefulSet", statefulSet);
        data.put("pods", PodFormat.formatPodList(pods));
        data.put("events", events);

        //封装返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("code", 1200);
        result.put("message", "获取 StatefulSet Resources 成功");
        result.put("data", data);

        return JSON.toJSONString(result);
    }
    @RequestMapping("/getStatefulSetLogs")
    public String getStatefulSetLogs(String name ,String namespace){

        //获取 StatefulSet 包含的 Pods
        List<Pod> pods = statefulSetsService.getPodStatefulSetInvolved(name, namespace);
        // 获取每个 Pod 的所有 Logs
        Map<String, Map<String, String>> podLogs = new HashMap<>();
        PodsServiceImpl podsService = new PodsServiceImpl();
        for(int i = 0; i < pods.size(); i++){
            podLogs.put(pods.get(i).getMetadata().getName(), podsService.getPodAllLogs(pods.get(i).getMetadata().getName(), namespace));
        }

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "获取 StatefulSet 日志成功");
        result.put("data", podLogs);

        return JSON.toJSONString(result);
    }
}

package com.nwu.controller.workload;

/**
 * @author Rex Joush
 * @time 2021.03.22
 */

import com.alibaba.fastjson.JSON;
import com.nwu.service.explorebalancing.impl.ServicesServiceImpl;
import com.nwu.service.impl.CommonServiceImpl;
import com.nwu.service.workload.impl.DaemonSetsServiceImpl;
import com.nwu.service.workload.impl.PodsServiceImpl;
import com.nwu.util.format.DaemonSetFormat;
import com.nwu.util.format.PodFormat;
import io.fabric8.kubernetes.api.model.Event;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.Service;
import io.fabric8.kubernetes.api.model.apps.DaemonSet;
import io.kubernetes.client.openapi.ApiException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.FileNotFoundException;
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

    @RequestMapping("/getAllDaemonSets")
    public String findAllDaemonSets(String namespace) throws ApiException {

        System.out.println(namespace);

        List<DaemonSet> daemonSets;

        if("".equals(namespace)){
            daemonSets = daemonSetsService.findAllDaemonSets();
        }else{
            daemonSets = daemonSetsService.findDaemonSetsByNamespace(namespace);
        }

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "获取 DaemonSet 列表成功");
        result.put("data", DaemonSetFormat.formatDaemonSetList(daemonSets));

        return JSON.toJSONString(result);

    }

    @RequestMapping("/getDaemonSetsByNamespace")
    public String findDaemonSetsByNamespace(String namespace) throws ApiException {

        List<DaemonSet> v1DaemonSetList = daemonSetsService.findDaemonSetsByNamespace(namespace);

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "获取 DaemonSet 列表成功");
        result.put("data", v1DaemonSetList);

        return JSON.toJSONString(result);

    }

    @RequestMapping("/getDaemonSetByNameAndNamespace")
    public String getDaemonSetByNameAndNamespace(String name, String namespace) throws ApiException {

        DaemonSet aDaemonSet = daemonSetsService.getDaemonSetByNameAndNamespace(name, namespace);

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "获取 DaemonSet 成功");
        result.put("data", aDaemonSet);

        return JSON.toJSONString(result);

    }

    @RequestMapping("/getDaemonSetResources")
    public String getDaemonSetResources(String name, String namespace){

        //获取 DaemonSet
        DaemonSet aDaemonSet = daemonSetsService.getDaemonSetByNameAndNamespace(name, namespace);

        //获取 DaemonSet 有关的 Pods
        List<Pod> pods = daemonSetsService.getPodDaemonSetInvolved(name, namespace);

        //获取 Services
        ServicesServiceImpl servicesService = new ServicesServiceImpl();
        List<Service> services = servicesService.getServicesByLabels(aDaemonSet.getSpec().getSelector().getMatchLabels());

        //获取事件
        List<Event> events = CommonServiceImpl.getEventByInvolvedObjectUid(aDaemonSet.getMetadata().getUid());

        //封装数据
        Map<String, Object> data = new HashMap<>();
        data.put("daemonSet", aDaemonSet);
        data.put("pods", PodFormat.formatPodList(pods));
        data.put("services", services);
        data.put("events", events);

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "通过name和namespace获取 DaemonSet 的 Resources 成功");
        result.put("data", data);

        return JSON.toJSONString(result);
    }

    @RequestMapping("/deleteDaemonSetByNameAndNamespace")
    public String deleteDaemonSetByNameAndNamespace(String name, String namespace) throws ApiException {

        Boolean delete = daemonSetsService.deleteDaemonSetByNameAndNamespace(name, namespace);

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "删除 DaemonSet 成功");
        result.put("data", delete);

        return JSON.toJSONString(result);

    }

    @RequestMapping("/createOrReplaceDaemonSet")
    public String createOrReplaceDaemonSet(String path) throws FileNotFoundException {
        DaemonSet aDaemonSet = daemonSetsService.createOrReplaceDaemonSet(path);

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "创建或更新 DaemonSet 成功");
        result.put("data", aDaemonSet);

        return JSON.toJSONString(result);
    }

    @RequestMapping("/createOrReplaceDaemonSetByYaml")
    public String createOrReplaceDaemonSetByYaml(String yaml) throws IOException {
        Boolean ok = daemonSetsService.createOrReplaceDaemonSetByYaml(yaml);

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", ok ? "创建或更新 DaemonSet 成功" : "创建或更新 DaemonSet 失败");

        return JSON.toJSONString(result);
    }

    @RequestMapping("/getDaemonSetYamlByNameAndNamespace")
    public String getDaemonSetYamlByNameAndNamespace(String name, String namespace){

        String daemonSetYaml = daemonSetsService.getDaemonSetYamlByNameAndNamespace(name, namespace);
        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "获取 DaemonSet Yaml 成功");
        result.put("data", daemonSetYaml);

        return JSON.toJSONString(result);
    }

    @RequestMapping("/getDaemonSetLogs")
    public String getDaemonSetLogs(String name ,String namespace){

        //获取 DaemonSet 包含的 Pods
        List<Pod> pods = daemonSetsService.getPodDaemonSetInvolved(name, namespace);
        System.out.println(pods.get(0));
        // 获取每个 Pod 的所有 Logs
        Map<String, Map<String, String>> podLogs = new HashMap<>();
        PodsServiceImpl podsService = new PodsServiceImpl();
        for(int i = 0; i < pods.size(); i++){
            podLogs.put(pods.get(i).getMetadata().getName(), podsService.getPodAllLogs(pods.get(i).getMetadata().getName(), namespace));
        }

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "获取 DaemonSet 日志成功");
        result.put("data", podLogs);

        return JSON.toJSONString(result);
    }
}

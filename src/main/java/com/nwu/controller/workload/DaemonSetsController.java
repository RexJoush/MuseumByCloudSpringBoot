package com.nwu.controller.workload;

/**
 * @author Rex Joush
 * @time 2021.03.22
 */

import com.alibaba.fastjson.JSON;
import com.nwu.service.explorebalancing.impl.ServicesServiceImpl;
import com.nwu.service.workload.impl.CronJobsServiceImpl;
import com.nwu.service.workload.impl.DaemonSetsServiceImpl;
import com.nwu.service.workload.impl.PodsServiceImpl;
import com.nwu.util.FilterPodsByControllerUid;
import com.nwu.util.KubernetesUtils;
import com.nwu.util.format.PodFormat;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.Service;
import io.fabric8.kubernetes.api.model.apps.DaemonSet;
import io.fabric8.kubernetes.api.model.batch.CronJob;
import io.fabric8.kubernetes.api.model.batch.Job;
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
 * Daemon Sets 的 controller 层
 */
@RestController
@RequestMapping("/daemonSets")
public class DaemonSetsController {
    @Resource
    private DaemonSetsServiceImpl daemonSetsService;

    @RequestMapping("/getAllDaemonSets")
    public String findAllDaemonSets(String namespace) throws ApiException {

        List<DaemonSet> daemonSets;

        if("".equals(namespace)){
            daemonSets = daemonSetsService.findAllDaemonSets();
        }else{
            daemonSets = daemonSetsService.findDaemonSetsByNamespace(namespace);
        }

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "获取 DaemonSet 列表成功");
        result.put("data", daemonSets);

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

//        System.out.println(name + namespace);
        //获取 DaemonSet
        DaemonSet aDaemonSet = daemonSetsService.getDaemonSetByNameAndNamespace(name, namespace);
        Map<String, String> matchLabels = aDaemonSet.getSpec().getSelector().getMatchLabels();
        String uid = aDaemonSet.getMetadata().getUid();

        //获取 Pods
        PodsServiceImpl podsService = new PodsServiceImpl();
        List<Pod> pods = FilterPodsByControllerUid.filterPodsByControllerUid(uid, podsService.findPodsByLabels(matchLabels));

        //获取 Services
        ServicesServiceImpl servicesService = new ServicesServiceImpl();
        List<Service> services = KubernetesUtils.client.services().withLabels(matchLabels).list().getItems();

        //封装数据
        Map<String, Object> data = new HashMap<>();
        data.put("daemonSet", aDaemonSet);
        data.put("pods", PodFormat.formatPodList(pods));
        data.put("services", services);

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

    @RequestMapping("/loadDaemonSetFromYaml")
    public String loadDaemonSetFromYaml(String path) throws ApiException, FileNotFoundException {

        DaemonSet daemonSet = daemonSetsService.loadDaemonSetFromYaml(path);

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "加载 DaemonSet 成功");
        result.put("data", daemonSet);

        return JSON.toJSONString(result);

    }

    @RequestMapping("/createDaemonSetByYaml")
    public String createDaemonSetByYaml(String path) throws ApiException, FileNotFoundException {

        DaemonSet daemonSet = daemonSetsService.createDaemonSetByYaml(path);

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "创建 DaemonSet 成功");
        result.put("data", daemonSet);

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

    @RequestMapping("/getDaemonSetYamlByNameAndNamespace")
    public String getDaemonSetYamlByNameAndNamespace(String name, String namespace){

        String daemonSetYaml = daemonSetsService.getDaemonSetYamlByNameAndNamespace(name, namespace);
        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "获取 DaemonSet Yaml 成功");
        result.put("data", daemonSetYaml);

        return JSON.toJSONString(result);
    }
}

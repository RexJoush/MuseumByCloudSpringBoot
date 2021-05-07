package com.nwu.controller.workload;

import com.alibaba.fastjson.JSON;
import com.nwu.entity.workload.PodDefinition;
import com.nwu.entity.workload.PodDetails;
import com.nwu.entity.workload.PodForm;
import com.nwu.service.workload.impl.PodsServiceImpl;
import io.fabric8.kubernetes.api.model.Pod;
import io.kubernetes.client.openapi.ApiException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Rex Joush
 * @time 2021.03.22
 */

/**
 * Pods 的 controller 层
 */

@RequestMapping("/pods")
@RestController
@CrossOrigin
public class PodsController {

    @Resource
    private PodsServiceImpl podsService;

    @RequestMapping("/changeResourceByYaml")
    public String changeResourceByYaml(@RequestBody String yaml){
        System.out.println(yaml);
        return "success";
    }

    @RequestMapping("/getAllPods")
    public String findAllPods(String namespace) {

        List<PodDefinition> pods;

        if ("".equals(namespace)){
            pods = podsService.findAllPods();
        } else {
            pods = podsService.findPodsByNamespace(namespace);
        }

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "获取 Pod 列表成功");
        result.put("data", pods);

        return JSON.toJSONString(result);

    }

    @RequestMapping("/getCompletePodsList")
    public String getCompletePodsList(String namespace) {

        List<Pod> pods;


        pods = podsService.findCompletePodsList();

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "获取完整 Pod 列表成功");
        result.put("data", pods);

        return JSON.toJSONString(result);

    }

    @RequestMapping("/getPodsByNode")
    public String findPodsByNode(String nodeName) {

        List<PodDefinition> pods = podsService.findPodsByNode(nodeName);

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "获取 Pod 列表成功");
        result.put("data", pods);

        return JSON.toJSONString(result);
    }

    @RequestMapping("/getPodByNameAndNamespace")
    public String findPodByNameAndNamespace(String name, String namespace) {

        PodDetails podByNameAndNamespace = podsService.findPodByNameAndNamespace(name, namespace);

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "获取 Pod 详情成功");
        result.put("data", podByNameAndNamespace);

        return JSON.toJSONString(result);
    }

    @RequestMapping("/getPodYamlByNameAndNamespace")
    public String getPodYamlByNameAndNamespace(String name, String namespace) {
        String podYaml = podsService.findPodYamlByNameAndNamespace(name, namespace);

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "获取 Pod Yaml 成功");
        result.put("data", podYaml);

        return JSON.toJSONString(result);
    }

    @RequestMapping("/getPodsByNamespace")
    public String findPodsByNamespace(String namespace) throws ApiException {

        List<PodDefinition> v1PodList = podsService.findPodsByNamespace(namespace);

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "获取 Pod 列表成功");
        result.put("data", v1PodList);

        return JSON.toJSONString(result);

    }


    @RequestMapping("/getPodBySvcLabel")
    public String findPodBySvcLabel(String labelKey, String labelValue) {

        List<PodDefinition> podLabel = podsService.findPodBySvcLabel(labelKey, labelValue);

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "通过 labels 获取  Pod  成功");
        result.put("data", podLabel);

        return JSON.toJSONString(result);
    }

    @RequestMapping("/getPodsByLabels")
    public String getPodsByLabels(String data) {


        System.out.println(data);
        Pod pods = new Pod();
//        List<Pod> pods = podsService.findPodsByLabels(labelsKey, labelsValue);

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "通过 labels 获取  Pods  成功");
        result.put("data", pods);

        return JSON.toJSONString(result);
    }

    @RequestMapping("/delPodByNameAndNamespace")
    public String deletePodByNameAndNamespace(String name, String namespace){
        Boolean delete = podsService.deletePodByNameAndNamespace(name, namespace);

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "删除 Pod 成功");
        result.put("data", delete);

        return JSON.toJSONString(result);
    }

    @RequestMapping("/loadPodFromYaml")
    public String loadPodFromYaml(String path) throws FileNotFoundException {

        Pod aPod = podsService.loadPodFromYaml(path);

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "加载 Pod 成功");
        result.put("data", aPod);

        return JSON.toJSONString(result);
    }

    @PostMapping("/createPodFromYamlFile")
    public String createPodFromYamlFile(@RequestParam("yaml") MultipartFile originalFile) throws FileNotFoundException {
        return JSON.toJSONString("弃用，在establish中使用");
    }

    @RequestMapping("/createOrReplacePod")
    public String createOrReplacePod(String path) throws FileNotFoundException {
        Pod aPod = podsService.createOrReplacePod(path);

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "创建或更新 Pod 成功");
        result.put("data", aPod);

        return JSON.toJSONString(result);
    }

    @RequestMapping("/getPodLogByNameAndNamespace")
    public String getPodLogByNameAndNamespace(String name, String namespace){
        String str = podsService.getPodLogByNameAndNamespace(name, namespace);

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "获取 Pod日志 成功");
        result.put("data", str);

        return JSON.toJSONString(result);
    }

    @RequestMapping("/createPodFromForm")
    public String createPodFromForm(PodForm podForm){
        return "弃用方法，在establish中使用";
    }

}

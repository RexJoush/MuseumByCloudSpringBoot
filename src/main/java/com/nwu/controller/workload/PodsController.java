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

        Map<String, Object> result = new HashMap<>();
        if(originalFile.isEmpty()){
            result.put("code", 1201);
            result.put("message", "文件为空");
            return JSON.toJSONString(result);
        }

        int code = 1200;
        File file = null;
        try {
            String originalFileName = originalFile.getOriginalFilename();
            String fileName[] = originalFileName.split("\\.",2);
            System.out.println(fileName[1]);
            file = File.createTempFile(fileName[0], fileName[1]);
            originalFile.transferTo(file);
            file.deleteOnExit();
            code = podsService.createPodByYamlFile(file);
        } catch (IOException e) {
            e.printStackTrace();
            code = 1204;
        }

        result.put("code", code);
        result.put("message", code == 1200 ? "创建成功" : code == 1202 ? "创建失败 请检查云平台相关信息" : "创建失败 请查看Yaml文件（name namespace等）");

        return JSON.toJSONString(result);
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

//    @RequestMapping("/createPodFromForm")
//    public String createPodFromForm(String name, String namespace, List<Object> labels, List<Object> annotations,
//                                    String secretName, String image, String imagePullPolicy, String command, String[] args,
//                                    String cpuLimit, String cpuRequest, String memoryLimit, String memoryRequest, Map<String, String> envVar, Integer amount) {
    @RequestMapping("/createPodFromForm")
    public String createPodFromForm(@RequestParam String podForm){
        System.out.println(podForm);

//        System.out.println( name);
//        System.out.println(namespace);
//        System.out.println(image);
//        System.out.println(labels);
//        System.out.println(annotations);
//        System.out.println(command);
//        System.out.println(args);
//        System.out.println(cpuLimit);
//        System.out.println(cpuRequest);
//        System.out.println(memoryLimit);
//        System.out.println(memoryRequest);
//        System.out.println(envVar);
//        System.out.println(amount);

        String name = "zqytest19";
        String namespace = "default";
        Map<String, String> labels = new HashMap<>();
        labels.put("name", "zqytest111");
        Map<String, String> annotations = new HashMap<>();
        annotations.put("name", "zqytest111");
        String secretName = "";
        String image = "nginx";
        String imagePullPolicy = "Always";
        String[] command = {""};
        String[] args ={""};
        String cpuLimit = "500m";
        String cpuRequest = "300m";
        String memoryLimit = "1000Gi";
        String memoryRequest = "512Mi";
        Map<String, String> envVar = new HashMap<>();
        envVar.put("name", "zqytest111");
        Integer amount = 2;
        PodForm podForm1 = new PodForm(name, namespace, labels, annotations, secretName, image, imagePullPolicy, command, args, cpuLimit, cpuRequest, memoryLimit, memoryRequest, envVar, amount);

        List<Pod> podList = podsService.createPodFromForm(podForm1);

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "创建 Pod 成功");
        result.put("data", podList);

        return JSON.toJSONString(result);

    }

}

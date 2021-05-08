package com.nwu.controller;

/**
 * @author Rex Joush
 * @time 2021.03.22
 */

import com.alibaba.fastjson.JSON;
import com.nwu.entity.workload.PodForm;
import com.nwu.service.workload.impl.PodsServiceImpl;
import io.fabric8.kubernetes.api.model.Pod;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 创建资源的 controller 层
 */
@RestController
@RequestMapping("/establish")
public class EstablishController {

    @Resource
    private PodsServiceImpl podsService;

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

    @RequestMapping(value = "/createPodFromForm")
    public String createPodFromForm(PodForm podForm){
//        System.out.println(podForm.getEnvKeys()[1]);
//        String name = "zqytest19";
//        String namespace = "default";
//        Map<String, String> labels = new HashMap<>();
//        labels.put("name", "zqytest111");
//        Map<String, String> annotations = new HashMap<>();
//        annotations.put("name", "zqytest111");
//        String secretName = "";
//        String image = "nginx";
//        String imagePullPolicy = "Always";
//        String[] command = {""};
//        String[] args ={""};
//        String cpuLimit = "500m";
//        String cpuRequest = "300m";
//        String memoryLimit = "1000Gi";
//        String memoryRequest = "512Mi";
//        Map<String, String> envVar = new HashMap<>();
//        envVar.put("name", "zqytest111");
//        Integer amount = 2;
//        PodForm podForm1 = new PodForm(name, namespace, labels, annotations, secretName, image, imagePullPolicy, command, args, cpuLimit, cpuRequest, memoryLimit, memoryRequest, envVar, amount);

        int code = podsService.createPodFromForm(podForm);

        Map<String, Object> result = new HashMap<>();

        result.put("code", code);
        if(code == 1200) result.put("message", "创建 Pod 成功");
        else result.put("message", "创建 Pod 失败，请检查是否重名");

        return JSON.toJSONString(result);

    }
}

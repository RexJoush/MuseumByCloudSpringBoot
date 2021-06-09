package com.nwu.controller;

/**
 * @author Rex Joush
 * @time 2021.03.22
 */

import com.alibaba.fastjson.JSON;
import com.nwu.entity.workload.Pod.PodForm;
import com.nwu.service.impl.CommonServiceImpl;
import com.nwu.service.workload.impl.PodsServiceImpl;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 创建资源的 controller 层
 */
@RestController
@RequestMapping("/establish")
public class EstablishController {

    @Resource
    private PodsServiceImpl podsService;
    private CommonServiceImpl commonService;

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
//            code = podsService.createPodByYamlFile(file);
            code = commonService.changeResourceByYaml(file);
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
        Pair<Integer, Boolean> pair = podsService.createPodFromForm(podForm);

        Map<String, Object> result = new HashMap<>();

        result.put("code", pair.getLeft());
        result.put("message", pair.getLeft() == 1200 ? "创建 Pod 成功" : "创建 Pod 失败，请检查是否重名");
        result.put("data", pair.getRight());

        return JSON.toJSONString(result);

    }
}

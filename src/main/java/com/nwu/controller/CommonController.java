package com.nwu.controller;

/**
 * @author Rex Joush
 * @time 2021.04.07
 */

import com.alibaba.fastjson.JSON;
import com.nwu.service.impl.CommonServiceImpl;
import com.nwu.util.KubernetesUtils;
import io.fabric8.kubernetes.api.model.Event;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 共有方法的 controller 层
 */
@RestController
@RequestMapping("/common")
public class CommonController {

    @Resource
    private CommonServiceImpl commonService;

    @RequestMapping("/changeResourceByYaml")
    public String changeResourceByYaml(@RequestBody String yaml) {
        Map<String, Object> result = new HashMap<>();
        int code = 0;
        // 将 \" 转换为 " 将 \\ 转换为 \
        // 即，去掉前后端传值时自动添加的转义字符
        String s1 = yaml.replaceFirst("[{]\"[A-Za-z]+\":\"", "");
        String substring = s1.substring(0, s1.length() - 2);
        System.out.println(substring);
        String s = substring.replaceAll("\\\\\"","\"").replaceAll("\\\\\\\\", "\\\\").replaceAll("\\\\n","%");
        System.out.println(s);

        File file = new File(KubernetesUtils.path);
        if (!file.getParentFile().exists()){
            file.getParentFile().mkdir();
        }
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(file);
            for (char c : s.toCharArray()) {
                if (c == '%'){
                    fileWriter.append("\r\n");
                }
                else {
                    fileWriter.append(c);
                }
            }
            fileWriter.close();

            code = commonService.changeResourceByYaml(file);

        } catch (IOException e) {
            e.printStackTrace();
            code = 1203;
        }

        result.put("code", code);
        result.put("message", "请求成功");

        return JSON.toJSONString(result);

    }


    @RequestMapping("/changeDeploymentByYaml")
    public String changeDeploymentByYaml(@RequestBody String yaml) {

        Map<String, Object> result = new HashMap<>();
        int code = 0;
        // 将 \" 转换为 " 将 \\ 转换为 \
        // 即，去掉前后端传值时自动添加的转义字符
        String s = yaml.substring(12, yaml.length() - 2).replaceAll("\\\\\"","\"").replaceAll("\\\\\\\\", "\\\\").replaceAll("\\\\n","%");
        File file = new File(KubernetesUtils.path);
        if (!file.getParentFile().exists()){
            file.getParentFile().mkdir();
        }
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(file);
            for (char c : s.toCharArray()) {
                if (c == '%'){
                    fileWriter.append("\r\n");
                }
                else {
                    fileWriter.append(c);
                }
            }
            fileWriter.close();

            code = commonService.changeDeploymentByYaml(file);

        } catch (IOException e) {
            e.printStackTrace();
            code = 1203;
        }

        result.put("code", code);
        result.put("message", "请求成功");

        return JSON.toJSONString(result);
    }
    @RequestMapping("/changeServicesByYaml")
    public String changeServicesByYaml(@RequestBody String yaml) {

        Map<String, Object> result = new HashMap<>();
        int code = 0;
        // 将 \" 转换为 " 将 \\ 转换为 \
        // 即，去掉前后端传值时自动添加的转义字符
        String s = yaml.substring(12, yaml.length() - 2).replaceAll("\\\\\"","\"").replaceAll("\\\\\\\\", "\\\\").replaceAll("\\\\n","%");
        File file = new File(KubernetesUtils.path);
        if (!file.getParentFile().exists()){
            file.getParentFile().mkdir();
        }
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(file);
            for (char c : s.toCharArray()) {
                if (c == '%'){
                    fileWriter.append("\r\n");
                }
                else {
                    fileWriter.append(c);
                }
            }
            fileWriter.close();

            code = commonService.changeServicesByYaml(file);

        } catch (IOException e) {
            e.printStackTrace();
            code = 1203;
        }

        result.put("code", code);
        result.put("message", "请求成功");

        return JSON.toJSONString(result);
    }
    @RequestMapping("/changeResourceByYaml_v1beta")
    public String changeResourceByYaml_v1beta(@RequestBody String yaml) {
        yaml=yaml.replaceFirst("v1(beta1)*","v1beta1");
        Map<String, Object> result = new HashMap<>();
        int code = 0;
        // 将 \" 转换为 " 将 \\ 转换为 \
        // 即，去掉前后端传值时自动添加的转义字符
        String s = yaml.substring(9, yaml.length() - 2).replaceAll("\\\\\"","\"").replaceAll("\\\\\\\\", "\\\\").replaceAll("\\\\n","%");
        File file = new File(KubernetesUtils.path);
        if (!file.getParentFile().exists()){
            file.getParentFile().mkdir();
        }
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(file);
            for (char c : s.toCharArray()) {
                if (c == '%'){
                    fileWriter.append("\r\n");
                }
                else {
                    fileWriter.append(c);
                }
            }
            fileWriter.close();

            code = commonService.changeResourceByYaml(file);

        } catch (IOException e) {
            e.printStackTrace();
            code = 1203;
        }

        result.put("code", code);
        result.put("message", "请求成功");

        return JSON.toJSONString(result);

    }

    @RequestMapping("/changeIngressesByYaml")
    public String changeIngressesByYaml(@RequestBody String yaml) {
        Map<String, Object> result = new HashMap<>();
        int code = 0;
        // 将 \" 转换为 " 将 \\ 转换为 \
        // 即，去掉前后端传值时自动添加的转义字符
        String s = yaml.substring(12, yaml.length() - 2).replaceAll("\\\\\"","\"").replaceAll("\\\\\\\\", "\\\\").replaceAll("\\\\n","%");
        File file = new File(KubernetesUtils.path);
        if (!file.getParentFile().exists()){
            file.getParentFile().mkdir();
        }
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(file);
            for (char c : s.toCharArray()) {
                if (c == '%'){
                    fileWriter.append("\r\n");
                }
                else {
                    fileWriter.append(c);
                }
            }
            fileWriter.close();

            code = commonService.changeIngressesByYaml(file);

        } catch (IOException e) {
            e.printStackTrace();
            code = 1203;
        }

        result.put("code", code);
        result.put("message", "请求成功");

        return JSON.toJSONString(result);
    }
    @PostMapping("/changeCrdObjectByYaml")
    public String changeCrdObjectByYaml(@RequestParam String yaml, @RequestParam String crdName) {
        Map<String, Object> result = new HashMap<>();
        int code = 0;
        // 将 \" 转换为 " 将 \\ 转换为 \
        // 即，去掉前后端传值时自动添加的转义字符
        String s = yaml.substring(0, yaml.length()-1).replaceAll("\\\\\"","\"").replaceAll("\\\\\\\\", "\\\\").replaceAll("\\\\n","%");
        System.out.println(s);
        File file = new File(KubernetesUtils.path);
        if (!file.getParentFile().exists()){
            file.getParentFile().mkdir();
        }
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(file);
            for (char c : s.toCharArray()) {
                if (c == '%'){
                    fileWriter.append("\r\n");
                }
                else {
                    fileWriter.append(c);
                }
            }
            fileWriter.close();

            code = commonService.changeCrdObjectByYaml(file,crdName);
            //code = commonService.changeResourceByYaml(file);

        } catch (IOException e) {
            e.printStackTrace();
            code = 1203;
        }

        result.put("code", code);
        result.put("message", "请求成功");
        return JSON.toJSONString(result);
    }

    @RequestMapping("/getEventByInvolvedObjectKKD")
    public String getEventByInvolvedObjectKKD(String name, String namespace, String kind){
        Pair<Integer, List<Event>> pair = commonService.getEventByInvolvedObjectNNK(name, namespace, kind);

        HashMap<String, Object> result = new HashMap<>();

        result.put("code", pair.getLeft());
        if(pair.getLeft() == 1200)
            result.put("message", "获取事件成功");
        else
            result.put("message", "获取事件成功");
        result.put("data", pair.getRight());

        return JSON.toJSONString(result);
    }

    @RequestMapping("/getEventByInvolvedObjectUid")
    public String getEventByInvolvedObjectUid(String uid){
        Pair<Integer, List<Event>> pair = commonService.getEventByInvolvedObjectUid(uid);

        HashMap<String, Object> result = new HashMap<>();

        result.put("code", pair.getLeft());
        if(pair.getLeft() == 1200)
            result.put("message", "获取事件成功");
        else
            result.put("message", "获取事件失败");
        result.put("data", pair.getRight());

        return JSON.toJSONString(result);
    }
}

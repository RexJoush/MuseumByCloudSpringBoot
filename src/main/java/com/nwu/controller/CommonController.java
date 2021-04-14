package com.nwu.controller;

/**
 * @author Rex Joush
 * @time 2021.04.07
 */

import com.alibaba.fastjson.JSON;
import com.nwu.service.impl.CommonServiceImpl;
import com.nwu.util.KubernetesUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.*;
import java.util.HashMap;
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

        System.out.println(yaml);

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


    @RequestMapping("/changeServicesByYaml")
    public String changeServicesByYaml(@RequestBody String yaml) {
        Map<String, Object> result = new HashMap<>();
        int code = 0;
        // 将 \" 转换为 " 将 \\ 转换为 \
        // 即，去掉前后端传值时自动添加的转义字符
        String s = yaml.substring(8, yaml.length() - 2).replaceAll("\\\\\"","\"").replaceAll("\\\\\\\\", "\\\\").replaceAll("\\\\n","%");
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

}

package com.nwu.controller;

/**
 * @author Rex Joush
 * @time 2021.04.07
 */

import com.alibaba.fastjson.JSON;
import com.nwu.service.impl.CommonServiceImpl;
import io.kubernetes.client.util.Yaml;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.*;

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

        String substring = yaml.substring(9, yaml.length() - 2).replaceAll("\n","\r\n");
        System.out.println(substring);
//        try {
//            FileOutputStream fos = new FileOutputStream("a.yaml");
//            fos.write(substring.getBytes());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


//        String s = commonService.changeResourceByYaml(yaml);

        return "success";
    }

}

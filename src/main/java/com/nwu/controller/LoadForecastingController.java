package com.nwu.controller;

/**
 * @author Rex Joush
 * @time 2021.03.22
 */

import com.alibaba.fastjson.JSON;
import com.nwu.service.LoadForecastingService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 负载预测的 controller 层
 */
@RestController
@RequestMapping("/loadForecasting")
public class LoadForecastingController {


    @Resource
    LoadForecastingService loadForecastingService;

    @RequestMapping("/expandShrinkPod")
    public String expandShrinkPod(@RequestParam("name") String name,
                                  @RequestParam("namespace") String namespace,
                                  @RequestParam("replicas") int replicas){
        System.out.println(name);
        System.out.println(namespace);
        System.out.println(replicas);

        String s = loadForecastingService.expandShrinkPod(name, namespace, replicas);

        Map<String, Object> result = new HashMap<>();

        if ("success".equals(s)){
            result.put("code", 1200);
            result.put("message", "修改副本数据成功");
        } else {
            result.put("code", 1202);
            result.put("message", "修改副本数据失败");
        }

        return JSON.toJSONString(result);
    }

}

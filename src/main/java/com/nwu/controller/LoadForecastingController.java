package com.nwu.controller;

/**
 * @author Rex Joush
 * @time 2021.03.22
 */

import com.alibaba.fastjson.JSON;
import com.nwu.service.LoadForecastingService;
import com.nwu.service.impl.LoadForecastingServiceImpl;
import com.nwu.service.workload.DeploymentsService;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
    DeploymentsService deploymentsService;

//    @RequestMapping("/expandShrinkPod")
//    public String expandShrinkPod(@RequestParam("name") String name,
//                                  @RequestParam("namespace") String namespace,
//                                  @RequestParam("replicas") int replicas){
//        System.out.println(name);
//        System.out.println(namespace);
//        System.out.println(replicas);
//
//        String s = loadForecastingService.expandShrinkPod(name, namespace, replicas);
//
//        Map<String, Object> result = new HashMap<>();
//
//        if ("success".equals(s)){
//            result.put("code", 1200);
//            result.put("message", "修改副本数据成功");
//        } else {
//            result.put("code", 1202);
//            result.put("message", "修改副本数据失败");
//        }
//
//        return JSON.toJSONString(result);
//    }
    @RequestMapping("/setForecast")
    public String setReplica(String date,String name, String namespace, String replica){
        Date date1=new Date(Long.parseLong(date));

        Pair<Integer, Boolean> pair=null;

        SimpleDateFormat sdf = new SimpleDateFormat();// 格式化时间
        sdf.applyPattern("yyyy-MM-dd HH:mm:ss a");// a为am/pm的标记
        Date nowdate = new Date();// 获取当前时间
        Date lastDate = new Date(date1 .getTime() - 60000);
        System.out.println(date1.getTime()+nowdate.getTime());
        System.out.println(date1.getTime()-nowdate.getTime());
        //判断执行时间与当前时间的差，小于一分钟立即执行，大于一分钟，一分钟前执行
        System.out.println(name+namespace+Integer.parseInt(replica));

        if (date1.getTime()-nowdate.getTime()<60000){
            System.out.println("1111");
            pair=deploymentsService.setReplicas(name, namespace, Integer.parseInt(replica));
        }else {
            System.out.println("2222");

            pair=loadForecastingService.timer(lastDate, name, namespace, replica);
        }

        Map<String, Object> result = new HashMap<>();

        result.put("code", pair.getLeft());
        if(pair.getLeft() == 1200) {
            result.put("message", "设置 Deployment的replica 成功");
        }else {
            result.put("message", "设置 Deployment的replica 失败");
        }
        result.put("data", pair.getRight());

        return JSON.toJSONString(result);
    }

}

package com.nwu.controller.workload;

import com.alibaba.fastjson.JSON;
import com.nwu.service.workload.impl.CronJobsServiceImpl;
import com.nwu.util.DealYamlStringFromFront;
import com.nwu.util.format.CronJobFormat;
import io.fabric8.kubernetes.api.model.batch.CronJob;
import io.kubernetes.client.openapi.ApiException;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Rex Joush
 * @time 2021.03.22
 */


/**
 * Cron Jobs的 controller 层
 */
@RestController
@RequestMapping("/cronJobs")
public class CronJobsController {
    @Resource
    private CronJobsServiceImpl cronJobsService;

    //增
    @RequestMapping("/createCronJobFromForm")
    public String createCronJobFromForm(){
        return "";
    }

    //删
    @RequestMapping("/deleteCronJobByNameAndNamespace")
    public String deleteCronJobByNameAndNamespace(String name, String namespace){

        Pair<Integer, Boolean> pair = cronJobsService.deleteCronJobByNameAndNamespace(name, namespace);

        Map<String, Object> result = new HashMap<>();

        result.put("code", pair.getLeft());
        if(pair.getLeft() == 1200) { result.put("message", "删除 CronJob 成功"); }
        else { result.put("message", "删除 CronJob 失败"); }
        result.put("data", pair.getRight());

        return JSON.toJSONString(result);
    }

    //改
    @RequestMapping("/changeCronJobByYamlString")
    public String changeCronJobByYamlString(@RequestBody String yaml) throws IOException {

        Map<String, Object> result = new HashMap<>();

        yaml = DealYamlStringFromFront.dealYamlStringFromFront(yaml);
        Pair<Integer, Boolean> pair = cronJobsService.createOrReplaceCronJobByYamlString(yaml);

        result.put("code", pair.getLeft());
        if(pair.getLeft() == 1200) result.put("message", "创建成功");
        else result.put("message", "创建失败");
        result.put("data", pair.getRight());

        return JSON.toJSONString(result);
    }

    //查
    @RequestMapping("/getAllCronJobs")
    public String findAllCronJobs(String namespace) throws ApiException {

        Pair<Integer, List<CronJob>> pair;
        if("".equals(namespace)){
            pair = cronJobsService.findAllCronJobs();
        }else{
            pair = cronJobsService.findCronJobsByNamespace(namespace);
        }

        Map<String, Object> result = new HashMap<>();

        if(pair.getLeft() == 1200) {
            result.put("message", "获取 CronJob 列表成功");
            result.put("data", CronJobFormat.formatCronJobList(pair.getRight()));
        }else {
            result.put("message", "获取 CronJob 列表失败");
            result.put("data", null);
        }
        result.put("code", pair.getLeft());

        return JSON.toJSONString(result);
    }
    @RequestMapping("/getCronJobByNameAndNamespace")
    public String getCronJobByNameAndNamespace(String name, String namespace){

        Pair<Integer, CronJob> pair = cronJobsService.getCronJobByNameAndNamespace(name, namespace);

        Map<String, Object> result = new HashMap<>();

        result.put("code", pair.getLeft());
        if(pair.getLeft() == 1200) {
            result.put("message", "通过名称和命名空间查找 CronJob 成功");
        }else {
            result.put("message", "通过名称和命名空间查找 CronJob 失败");
        }
        result.put("data", pair.getRight());

        return JSON.toJSONString(result);
    }
    @RequestMapping("/getCronJobYamlByNameAndNamespace")
    public String getCronJobYamlByNameAndNamespace(String name, String namespace){

        Pair<Integer, String> pair = cronJobsService.getCronJobYamlByNameAndNamespace(name, namespace);

        Map<String, Object> result = new HashMap<>();

        result.put("code", pair.getLeft());
        if(pair.getLeft() == 1200) {
            result.put("message", "获取 CronJob Yaml 成功");
        }else {
            result.put("message", "获取 CronJob Yaml 失败");
        }
        result.put("data", pair.getRight());

        return JSON.toJSONString(result);
    }
    @RequestMapping("/getCronJobResources")
    public String getCronJobResources(String name, String namespace){

        Pair<Integer, Map> pair = cronJobsService.getCronJobResources(name, namespace);

        Map<String, Object> result = new HashMap<>();

        result.put("code", pair.getLeft());
        if(pair.getLeft() == 1200){
            result.put("message", "获取成功");
        }else if(pair.getLeft() == 1201){
            result.put("message", "获取失败");
        } else if(pair.getLeft() == 1202){
            result.put("message", "您的操作有误");
        }else{
            result.put("message", "获取到部分资源");
        }
        result.put("data", pair.getRight());

        return JSON.toJSONString(result);
    }
}

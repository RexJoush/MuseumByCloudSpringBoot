package com.nwu.controller.workload;

import com.alibaba.fastjson.JSON;
import com.nwu.service.workload.impl.CronJobsServiceImpl;
import io.fabric8.kubernetes.api.model.batch.CronJob;
import io.kubernetes.client.openapi.ApiException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
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

    @RequestMapping("/getAllCronJobs")
    public String findAllCronJobs(String namespace) throws ApiException {

        List<CronJob> cronJobList;

        if("".equals(namespace)){
            cronJobList = cronJobsService.findAllCronJobs();
        }else{
            cronJobList = cronJobsService.findCronJobsByNamespace(namespace);
        }

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "获取 CronJob 列表成功");
        result.put("data", cronJobList);

        return JSON.toJSONString(result);

    }

    @RequestMapping("/getCronJobsByNamespace")
    public String findCronJobsByNamespace(String namespace) throws ApiException {

        List<CronJob> v1CronJobList = cronJobsService.findCronJobsByNamespace(namespace);

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "获取 CronJob 列表成功");
        result.put("data", v1CronJobList);

        return JSON.toJSONString(result);

    }

    @RequestMapping("/deleteCronJobByNameAndNamespace")
    public String deleteCronJobByNameAndNamespace(String name, String namespace){
        Boolean delete = cronJobsService.deleteCronJobByNameAndNamespace(name, namespace);

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "删除 CronJob 成功");
        result.put("data", delete);

        return JSON.toJSONString(result);
    }

    @RequestMapping("/loadCronJobFromYaml")
    public String loadCronJobFromYaml(String path) throws FileNotFoundException {

        CronJob aCronJob = cronJobsService.loadCronJobFromYaml(path);

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "加载 CronJob 成功");
        result.put("data", aCronJob);

        return JSON.toJSONString(result);
    }

    @RequestMapping("/createCronJobFromYaml")
    public String createCronJobFromYaml(String path) throws FileNotFoundException {

        CronJob aCronJob = cronJobsService.createCronJobByYaml(path);

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "创建 CronJob 成功");
        result.put("data", aCronJob);

        return JSON.toJSONString(result);
    }

    @RequestMapping("/createOrReplaceCronJob")
    public String createOrReplaceCronJob(String path) throws FileNotFoundException {
        CronJob aCronJob = cronJobsService.createOrReplaceCronJob(path);

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "创建或更新 CronJob 成功");
        result.put("data", aCronJob);

        return JSON.toJSONString(result);
    }

    @RequestMapping("/getCronJobByNameAndNamespace")
    public String getCronJobByNameAndNamespace(String name, String namespace){
        CronJob aCronJob = cronJobsService.getCronJobByNameAndNamespace(name, namespace);

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "通过名称和命名空间查找 CronJob 成功");
        result.put("data", aCronJob);

        return JSON.toJSONString(result);
    }

    @RequestMapping("/getCronJobYamlByNameAndNamespace")
    public String getCronJobYamlByNameAndNamespace(String name, String namespace){

        String cronJobYaml = cronJobsService.getCronJobYamlByNameAndNamespace(name, namespace);
        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "获取 CronJob Yaml 成功");
        result.put("data", cronJobYaml);

        return JSON.toJSONString(result);
    }

}

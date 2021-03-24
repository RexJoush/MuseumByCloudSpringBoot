package com.nwu.controller.workload;

/**
 * @author Rex Joush
 * @time 2021.03.22
 */

import com.alibaba.fastjson.JSON;
import com.nwu.service.workload.JobsService;
import com.nwu.service.workload.impl.DeploymentsServiceImpl;
import com.nwu.service.workload.impl.JobsServiceImpl;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.batch.CronJob;
import io.fabric8.kubernetes.api.model.batch.Job;
import io.kubernetes.client.openapi.ApiException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Jobs 的 controller 层
 */
@RestController
@RequestMapping("/jobs")
public class JobsController {
    @Resource
    private JobsServiceImpl jobsService;

    @RequestMapping("/getAllJobs")
    public String findAllJobs() throws ApiException {

        List<Job> jobs = jobsService.findAllJobs();

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "获取 Job 列表成功");
        result.put("data", jobs);

        return JSON.toJSONString(result);

    }

    @RequestMapping("/getJobsByNamespace")
    public String findJobsByNamespace(String namespace) throws ApiException {

        List<Job> v1JobList = jobsService.findJobsByNamespace(namespace);

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "获取 Job 列表成功");
        result.put("data", v1JobList);

        return JSON.toJSONString(result);

    }

    @RequestMapping("/deleteJobByNameAndNamespace")
    public String deleteJobByNameAndNamespace(String name, String namespace){
        Boolean delete = jobsService.deleteJobByNameAndNamespace(name, namespace);

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "删除 Job 成功");
        result.put("data", delete);

        return JSON.toJSONString(result);
    }

    @RequestMapping("/loadJobFromYaml")
    public String loadJobFromYaml(InputStream yamlInputStream){

        Job aJob = jobsService.loadJobFromYaml(yamlInputStream);

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "加载 Job 成功");
        result.put("data", aJob);

        return JSON.toJSONString(result);
    }

    @RequestMapping("/createJobFromYaml")
    public String createJobFromYaml(InputStream yamlInputStream){

        Job aJob = jobsService.createJobByYaml(yamlInputStream);

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "创建 Job 成功");
        result.put("data", aJob);

        return JSON.toJSONString(result);
    }

    @RequestMapping("/createOrReplaceJob")
    public String createOrReplaceJob(InputStream yamlInputStream){
        Job aJob = jobsService.createOrReplaceJob(yamlInputStream);

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "创建或更新 Job 成功");
        result.put("data", aJob);

        return JSON.toJSONString(result);
    }

}

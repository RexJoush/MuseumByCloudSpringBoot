package com.nwu.controller.workload;

/**
 * @author Rex Joush
 * @time 2021.03.22
 */

import com.alibaba.fastjson.JSON;
import com.nwu.service.workload.impl.JobsServiceImpl;
import com.nwu.service.workload.impl.PodsServiceImpl;
import com.nwu.util.DealYamlStringFromFront;
import com.nwu.util.format.JobFormat;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.batch.Job;
import io.kubernetes.client.openapi.ApiException;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.FileNotFoundException;
import java.io.IOException;
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

    //增
    @RequestMapping("/createJobFromYaml")
    public String createJobFromYaml(String path) throws FileNotFoundException {

        Job aJob = jobsService.createJobByYaml(path);

        Map<String, Object> result = new HashMap<>();
        if(aJob != null) {
            result.put("code", 1200);
            result.put("message", "创建 Job 成功");
        }else {
            result.put("code", 1299);
            result.put("message", "创建 Job 失败");
        }

        return JSON.toJSONString(result);
    }

    //删
    @RequestMapping("/deleteJobByNameAndNamespace")
    public String deleteJobByNameAndNamespace(String name, String namespace){

        Pair<Integer, Boolean> pair = jobsService.deleteJobByNameAndNamespace(name, namespace);

        Map<String, Object> result = new HashMap<>();

        result.put("code", pair.getLeft());
        if(pair.getLeft() == 1200) {
            result.put("message", "删除 Job 成功");
        }else {
            result.put("message", "删除 Job 失败");
        }
        result.put("data", pair.getRight());

        return JSON.toJSONString(result);
    }

    //改
    @RequestMapping("/changeJobByYamlString")
    public String changeJobByYamlString(@RequestBody String yaml) throws IOException, ApiException {

        Map<String, Object> result = new HashMap<>();

        yaml = DealYamlStringFromFront.dealYamlStringFromFront(yaml);
        Pair<Integer, Boolean> pair = jobsService.createOrReplaceJobByYamlString(yaml);

        result.put("code", pair.getLeft());
        if(pair.getLeft() == 1200) result.put("message", "创建成功");
        else result.put("message", "创建失败");
        result.put("data", pair.getRight());

        return JSON.toJSONString(result);
    }

    //查
    @RequestMapping("/getAllJobs")
    public String findAllJobs(String namespace) throws ApiException {

        Pair<Integer, List<Job>> pair;

        if("".equals(namespace)){
            pair = jobsService.findAllJobs();
        }else{
            pair = jobsService.findJobsByNamespace(namespace);
        }

        Map<String, Object> result = new HashMap<>();

        result.put("code", pair.getLeft());
        if(pair.getLeft() == 1200) {
            result.put("message", "获取 Job 列表成功");
            result.put("data", JobFormat.formatJobList(pair.getRight()));
        }else {
            result.put("message", "获取 Job 列表失败");
            result.put("data", null);
        }

        return JSON.toJSONString(result);
    }
    @RequestMapping("/getJobsByNamespace")
    public String findJobsByNamespace(String namespace) throws ApiException {

        Pair<Integer, List<Job>> pair = jobsService.findJobsByNamespace(namespace);

        Map<String, Object> result = new HashMap<>();

        result.put("code", pair.getLeft());
        if(pair.getLeft() == 1200) {
            result.put("message", "获取 Job 列表成功");
        }else {
            result.put("message", "获取 Job 列表失败");
        }
        result.put("data", pair.getRight());

        return JSON.toJSONString(result);
    }
    @RequestMapping("/getJobByNameAndNamespace")
    public String getJobByNameAndNamespace(String name, String namespace){

        Pair<Integer, Job> pair = jobsService.getJobByNameAndNamespace(name, namespace);

        Map<String, Object> result = new HashMap<>();

        result.put("code", pair.getLeft());
        if(pair.getLeft() == 1200) {
            result.put("message", "获取 Job 成功");
        }else {
            result.put("message", "获取 Job 失败");
        }
        result.put("data", pair.getRight());

        return JSON.toJSONString(result);
    }
    @RequestMapping("/getJobResources")
    public String getJobResources(String name, String namespace){

        Pair<Integer, Map> pair = jobsService.getJobResources(name, namespace);

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
    @RequestMapping("/getJobYamlByNameAndNamespace")
    public String getJobYamlByNameAndNamespace(String name, String namespace){

        Pair<Integer, String> pair = jobsService.getJobYamlByNameAndNamespace(name, namespace);

        Map<String, Object> result = new HashMap<>();

        result.put("code", pair.getLeft());
        if(pair.getLeft() == 1200) {
            result.put("message", "获取 Job Yaml 成功");
        }else {
            result.put("message", "获取 Job Yaml 失败");
        }
        result.put("data", pair.getRight());

        return JSON.toJSONString(result);
    }
    @RequestMapping("/getJobLogs")
    public String getJobLogs(String name ,String namespace){

        //获取 Job 包含的 Pods
        Pair<Integer, List<Pod>> pair = jobsService.getPodJobInvolved(name, namespace);

        Map<String, Object> result = new HashMap<>();
        if(pair.getLeft() == 1200) {
            // 获取每个 Pod 的所有 Logs
            Map<String, Map<String, String>> podLogs = new HashMap<>();
            PodsServiceImpl podsService = new PodsServiceImpl();
            List<Pod> pods = pair.getRight();
            for(int i = 0; i < pods.size(); i++){
                podLogs.put(pods.get(i).getMetadata().getName(), podsService.getPodAllLogs(pods.get(i).getMetadata().getName(), namespace));
            }
            result.put("code", 1200);
            result.put("message", "获取 Job 日志成功");
            result.put("data", podLogs);
        }else {
            result.put("code", 1201);
            result.put("message", "获取 Job 日志失败");
            result.put("data", null);
        }

        return JSON.toJSONString(result);
    }
}

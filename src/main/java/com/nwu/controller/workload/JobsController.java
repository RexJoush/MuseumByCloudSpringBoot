package com.nwu.controller.workload;

/**
 * @author Rex Joush
 * @time 2021.03.22
 */

import com.alibaba.fastjson.JSON;
import com.nwu.service.impl.CommonServiceImpl;
import com.nwu.service.workload.impl.JobsServiceImpl;
import com.nwu.service.workload.impl.PodsServiceImpl;
import com.nwu.util.format.JobFormat;
import com.nwu.util.format.PodFormat;
import io.fabric8.kubernetes.api.model.Event;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.batch.Job;
import io.kubernetes.client.openapi.ApiException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.FileNotFoundException;
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
    public String findAllJobs(String namespace) throws ApiException {

        List<Job> jobs;

        if("".equals(namespace)){
            jobs = jobsService.findAllJobs();
        }else{
            jobs = jobsService.findJobsByNamespace(namespace);
        }

        System.out.println(JobFormat.formatJobList(jobs));

        Map<String, Object> result = new HashMap<>();

        System.out.println("进入了findAllJobs");
        result.put("code", 1200);
        result.put("message", "获取 Job 列表成功");
        result.put("data", JobFormat.formatJobList(jobs));

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

    @RequestMapping("/getJobByNameAndNamespace")
    public String getJobByNameAndNamespace(String name, String namespace){

        Job aJob = jobsService.getJobByNameAndNamespace(name, namespace);

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "通过name和namespace获取 Job 成功");
        result.put("data", aJob);

        return JSON.toJSONString(result);
    }

    @RequestMapping("/getJobResources")
    public String getJobResources(String name, String namespace){

        Job aJob = jobsService.getJobByNameAndNamespace(name, namespace);
        PodsServiceImpl podsService = new PodsServiceImpl();
        Map<String, String> matchLabels = aJob.getSpec().getSelector().getMatchLabels();
        List<Pod> pods = podsService.findPodsByLabels(matchLabels);

        //获取事件
        List<Event> events = CommonServiceImpl.getEventByInvolvedObjectUid(aJob.getMetadata().getUid());

        HashMap<String, Object> data = new HashMap<>();
        data.put("job", aJob);
        data.put("pods", PodFormat.formatPodList(pods));
        data.put("events", events);


        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "通过name和namespace获取 Job 和 Pods 成功");
        result.put("dataJob", data);

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
    public String loadJobFromYaml(String path) throws FileNotFoundException {

        Job aJob = jobsService.loadJobFromYaml(path);

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "加载 Job 成功");
        result.put("data", aJob);

        return JSON.toJSONString(result);
    }

    @RequestMapping("/createJobFromYaml")
    public String createJobFromYaml(String path) throws FileNotFoundException {

        Job aJob = jobsService.createJobByYaml(path);

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "创建 Job 成功");
        result.put("data", aJob);

        return JSON.toJSONString(result);
    }

    @RequestMapping("/createOrReplaceJob")
    public String createOrReplaceJob(String path) throws FileNotFoundException {
        Job aJob = jobsService.createOrReplaceJob(path);

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "创建或更新 Job 成功");
        result.put("data", aJob);

        return JSON.toJSONString(result);
    }

    @RequestMapping("/getJobYamlByNameAndNamespace")
    public String getJobYamlByNameAndNamespace(String name, String namespace){

        String jobYaml = jobsService.getJobYamlByNameAndNamespace(name, namespace);
        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "获取 Job Yaml 成功");
        result.put("data", jobYaml);

        return JSON.toJSONString(result);
    }

    @RequestMapping("/getJobLogs")
    public String getJobLogs(String name ,String namespace){

        //获取 Job 包含的 Pods
        List<Pod> pods = jobsService.getPodJobInvolved(name, namespace);
        // 获取每个 Pod 的所有 Logs
        Map<String, Map<String, String>> podLogs = new HashMap<>();
        PodsServiceImpl podsService = new PodsServiceImpl();
        for(int i = 0; i < pods.size(); i++){
            podLogs.put(pods.get(i).getMetadata().getName(), podsService.getPodAllLogs(pods.get(i).getMetadata().getName(), namespace));
        }

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "获取 Job 日志成功");
        result.put("data", podLogs);

        return JSON.toJSONString(result);
    }
}

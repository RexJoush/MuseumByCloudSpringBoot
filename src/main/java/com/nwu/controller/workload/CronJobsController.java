package com.nwu.controller.workload;

import com.alibaba.fastjson.JSON;
import com.nwu.entity.workload.JobInformation;
import com.nwu.service.impl.CommonServiceImpl;
import com.nwu.service.workload.impl.CronJobsServiceImpl;
import com.nwu.service.workload.impl.JobsServiceImpl;
import com.nwu.service.workload.impl.PodsServiceImpl;
import com.nwu.util.KubernetesUtils;
import com.nwu.util.format.CronJobFormat;
import com.nwu.util.format.JobFormat;
import com.nwu.util.format.PodFormat;
import io.fabric8.kubernetes.api.model.Event;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.batch.CronJob;
import io.fabric8.kubernetes.api.model.batch.Job;
import io.kubernetes.client.openapi.ApiException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
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

    //增方法
    @RequestMapping("/createCronJobFromYaml")
    public String createCronJobFromYaml(String path) throws FileNotFoundException {

        CronJob aCronJob = cronJobsService.createCronJobByYaml(path);

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "创建 CronJob 成功");
        result.put("data", aCronJob);

        return JSON.toJSONString(result);
    }

    //删方法
    @RequestMapping("/deleteCronJobByNameAndNamespace")
    public String deleteCronJobByNameAndNamespace(String name, String namespace){
        Boolean delete = cronJobsService.deleteCronJobByNameAndNamespace(name, namespace);

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "删除 CronJob 成功");
        result.put("data", delete);

        return JSON.toJSONString(result);
    }

    //改方法
    @RequestMapping("/createOrReplaceCronJob")
    public String createOrReplaceCronJob(String path) throws FileNotFoundException {
        CronJob aCronJob = cronJobsService.createOrReplaceCronJob(path);

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "创建或更新 CronJob 成功");
        result.put("data", aCronJob);

        return JSON.toJSONString(result);
    }

    //查方法
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
        result.put("data", CronJobFormat.formatCronJobList(cronJobList));

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
    @RequestMapping("/getCronJobResources")
    public String getCronJobResources(String name, String namespace){

        CronJob aCronJob = cronJobsService.getCronJobByNameAndNamespace(name, namespace);
        String cronJobUid = aCronJob.getMetadata().getUid();

        //获取CronJob下的Jobs 并放在前amount个位置
        List<Job> jobList = KubernetesUtils.client.batch().jobs().inAnyNamespace().list().getItems();
        int amount = 0;
        for(int i = 0; i < jobList.size(); i ++){
            Job tmp = jobList.get(i);
            for(int j = tmp.getMetadata().getOwnerReferences().size() - 1; j >= 0; j --){
                try{
                    if(tmp.getMetadata().getOwnerReferences().get(j).getUid().equals(cronJobUid)){
                        jobList.set(amount ++, tmp);
                        break;
                    }
                }catch(Exception e){
                    System.out.println("CronJobController-getCronJobResources-可能Job没有OwnerReferences及以下选项");
                }
            }
        }

        //标准化Job,并截取CronJob下的Job 前amount个
        List<JobInformation> jobInformationList = JobFormat.formatJobList(jobList.subList(0, amount));

        //将正在运行的Jobs放在列表后面
        int i = 0;
        int j = amount - 1;
        while(i < j){
            while(i < j){
                if(jobInformationList.get(i).getRunningPods() > 0) break;
                i ++;
            }
            while(i < j){
                if(jobInformationList.get(j).getRunningPods() == 0) break;
                j --;
            }
            //交换，运行中Job在后
            JobInformation jobInformation = new JobInformation();
            jobInformation = jobInformationList.get(i);
            jobInformationList.set(i, jobInformationList.get(j));
            jobInformationList.set(j, jobInformation);
        }
        int mid = jobInformationList.get(i).getRunningPods() > 0 ? i : i + 1;//分割非运行与运行中

        //获取事件
        List<Event> events = CommonServiceImpl.getEventByInvolvedObjectUid(cronJobUid);

        //放入数据
        Map<String, Object> data = new HashMap<>();
        data.put("cronJob", aCronJob);
        data.put("jods", jobInformationList.subList(0, mid));
        data.put("runningJods", jobInformationList.subList(mid, amount));
        data.put("events",events);

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "通过name和namespace获取 cronJob 的 Resources 成功");
        result.put("data", data);

        return JSON.toJSONString(result);
    }

}

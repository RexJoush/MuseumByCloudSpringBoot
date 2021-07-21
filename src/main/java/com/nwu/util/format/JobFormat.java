package com.nwu.util.format;

/**
 * @author zqy
 * @time 2021.04.16
 */

import com.nwu.entity.workload.Job.JobInformation;
import com.nwu.service.workload.impl.PodsServiceImpl;
import com.nwu.util.FilterPodsByControllerUid;
import com.nwu.util.TimeTransformation;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.batch.Job;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 处理并返回一个前端需要的 Job 对象
 */
public class JobFormat {

    public static List<JobInformation> formatJobList(List<Job> jobList){
        if(jobList == null) return null;
        List<JobInformation> jobInformationList = new ArrayList<>();


        Iterator<Job> iterator = jobList.iterator();
        List<Integer> test = new ArrayList<>();
        while(iterator.hasNext()){

            //获取Job
            Job aJob = iterator.next();
            Map<String, String> matchLabels = aJob.getSpec().getSelector().getMatchLabels();

            //获取Job 对应 Pods
            PodsServiceImpl podsService = new PodsServiceImpl();
            List<Pod> pods = podsService.findPodsByLabels(matchLabels);
            pods = FilterPodsByControllerUid.filterPodsByControllerUid(aJob.getMetadata().getUid(), pods);

            String status = "1";
            int runningPods = 0;
            for(int i = 0; i < pods.size(); i ++){
                Pod tmpPod = pods.get(i);
                if(tmpPod.getStatus().getPhase().equals("Running")){
                    runningPods += 1;
                }
                else if(tmpPod.getStatus().getPhase().equals("Pending")){
                    status = "0";
                }
            }

            //标准化Job
            JobInformation jobInformation = new JobInformation();
            jobInformation.setName(aJob.getMetadata().getName() == null ? "未知" : aJob.getMetadata().getName());
            jobInformation.setNamespace(aJob.getMetadata().getNamespace() == null ? "未知" : aJob.getMetadata().getNamespace());
            jobInformation.setCreationTimestamp(aJob.getMetadata().getCreationTimestamp() == null ? "未知" : TimeTransformation.UTCToCST(aJob.getMetadata().getCreationTimestamp(), TimeTransformation.FORMAT));
            jobInformation.setStatus(status);
            jobInformation.setRunningPods(runningPods);
            jobInformation.setReplicas(aJob.getSpec().getCompletions() == null ? 0 : aJob.getSpec().getCompletions());

            jobInformationList.add(jobInformation);
        }
        return jobInformationList;
    }
}

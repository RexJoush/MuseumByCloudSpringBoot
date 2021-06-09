package com.nwu.util.format;

import com.nwu.entity.workload.CronJob.CronJobInformation;
import io.fabric8.kubernetes.api.model.batch.CronJob;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zqy
 * @time 2021.04.19
 */
public class CronJobFormat {
    public static List<CronJobInformation> formatCronJobList(List<CronJob> cronJobList){
        if(cronJobList == null) return null;
        List<CronJobInformation> cronJobInformationList = new ArrayList<>();

        for(int i = 0; i < cronJobList.size(); i ++){
            CronJobInformation cronJobInformation = new CronJobInformation();

            cronJobInformation.setName(cronJobList.get(i).getMetadata().getName() == null ? "未知" : cronJobList.get(i).getMetadata().getName());
            cronJobInformation.setNamespace(cronJobList.get(i).getMetadata().getNamespace() == null ? "未知" : cronJobList.get(i).getMetadata().getNamespace());
            cronJobInformation.setStatus("1");
            cronJobInformation.setSchedule(cronJobList.get(i).getSpec().getSchedule() == null ? "未知" : cronJobList.get(i).getSpec().getSchedule());
            cronJobInformation.setRunningJobs(cronJobList.get(i).getStatus().getActive() == null ? 0 : cronJobList.get(i).getStatus().getActive().size());
            cronJobInformation.setLastSchedulingTime(cronJobList.get(i).getStatus().getLastScheduleTime() == null ? "未知" : cronJobList.get(i).getStatus().getLastScheduleTime());
            cronJobInformation.setCreationTimestamp(cronJobList.get(i).getMetadata().getCreationTimestamp() == null ? "未知" : cronJobList.get(i).getMetadata().getCreationTimestamp());

            cronJobInformationList.add(cronJobInformation);
        }

        return cronJobInformationList;
    }

    public static List<CronJobInformation> formatCronJobListDetailed(List<CronJob> cronJobList){
        List<CronJobInformation> cronJobInformationList = CronJobFormat.formatCronJobList(cronJobList);

        for (int i = 0; i < cronJobInformationList.size(); i++) {
            cronJobInformationList.get(i).setUid(cronJobList.get(i).getMetadata().getUid());
            cronJobInformationList.get(i).setLabels(cronJobList.get(i).getMetadata().getLabels());
            cronJobInformationList.get(i).setAnnotation(cronJobList.get(i).getMetadata().getAnnotations());
            cronJobInformationList.get(i).setSuSpend(cronJobList.get(i).getSpec().getSuspend() == true ? "True" : "False");
            cronJobInformationList.get(i).setConcurrencyPolicy(cronJobList.get(i).getSpec().getConcurrencyPolicy());
        }

        return cronJobInformationList;
    }
}

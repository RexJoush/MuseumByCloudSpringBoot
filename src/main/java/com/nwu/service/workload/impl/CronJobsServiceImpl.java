package com.nwu.service.workload.impl;

import com.nwu.entity.workload.Job.JobInformation;
import com.nwu.service.impl.CommonServiceImpl;
import com.nwu.service.workload.CronJobsService;
import com.nwu.util.KubernetesUtils;
import com.nwu.util.format.JobFormat;
import io.fabric8.kubernetes.api.model.Event;
import io.fabric8.kubernetes.api.model.batch.CronJob;
import io.fabric8.kubernetes.api.model.batch.Job;
import io.kubernetes.client.util.Yaml;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.nwu.util.GetYamlInputStream.byPath;

/**
 * @author Rex Joush
 * @time 2021.03.22
 */

/**
 * Cron Jobs 的 service 层实现类
 */
@Service
public class CronJobsServiceImpl implements CronJobsService {

    @Override
    public Pair<Integer, List<CronJob>> findAllCronJobs(){
        try{
            List<CronJob> items = KubernetesUtils.client.batch().cronjobs().inAnyNamespace().list().getItems();
            return Pair.of(1200, items);
        }catch(Exception e){
            System.out.println("查找CronJobs失败，在CronJobsServiceImpl类的findAllCronJobs方法中");
        }
        return Pair.of(1201, null);
    }

    @Override
    public Pair<Integer, List<CronJob>> findCronJobsByNamespace(String namespace) {
        try{
            List<CronJob> items = KubernetesUtils.client.batch().cronjobs().inNamespace(namespace).list().getItems();
            return Pair.of(1200, items);
        }catch(Exception e){
            System.out.println("查找CronJobs失败，在CronJobsServiceImpl类的findCronJobsByNamespace方法中");
        }
        return Pair.of(1201, null);
    }

    @Override
    public Pair<Integer, Boolean> deleteCronJobByNameAndNamespace(String name, String namespace){
        try{
            Boolean delete = KubernetesUtils.client.batch().cronjobs().inNamespace(namespace).withName(name).delete();
            return Pair.of(1200, delete);
        }catch(Exception e){
            System.out.println("删除CronJob在失败，CronJobsServiceImpl类的deleteCronJobByNameAndNamespace方法中");
        }
        return Pair.of(1201, null);
    }

    @Override
    public Pair<Integer, CronJob> loadCronJobFromYaml(String path) throws FileNotFoundException {
        InputStream yamlInputStream = byPath(path);
        try{
            CronJob cronJob = KubernetesUtils.client.batch().cronjobs().load(yamlInputStream).get();
            return Pair.of(1200, cronJob);
        }catch(Exception e){
            System.out.println("加载CronJob失败，在CronJobsServiceImpl类的loadCronJobFromYaml方法中");
        }
        return Pair.of(1201, null);
    }

    @Override
    public Pair<Integer, Boolean> createOrReplaceCronJobByYamlString(String yaml){
        try{
            CronJob cronJob = Yaml.loadAs(yaml, CronJob.class);
            KubernetesUtils.client.batch().cronjobs().inNamespace(cronJob.getMetadata().getNamespace()).withName(cronJob.getMetadata().getName()).createOrReplace(cronJob);
            return Pair.of(1200, true);
        }catch (Exception e){
            System.out.println("创建 CronJob 失败，请检查 Yaml 格式或是否重名，在 CronJobsServiceImpl 类的 createOrReplaceCronJobByYamlString 方法中");
        }
        return Pair.of(1201, null);
    }

    @Override
    public CronJob createCronJobByYaml(String path) throws FileNotFoundException {
        InputStream yamlInputStream = byPath(path);

        try {
            CronJob cronJob = KubernetesUtils.client.batch().cronjobs().load(yamlInputStream).get();
            String nameSpace = cronJob.getMetadata().getNamespace();
            cronJob = KubernetesUtils.client.batch().cronjobs().inNamespace(nameSpace).create(cronJob);
            return cronJob;
        }catch(Exception e){
            System.out.println("创建CronJob失败，缺少必要的命名空间参数，或是已经有相同的资源对象，在CronJobsServiceImpl类的createCronJobByYaml方法");
        }
        return null;
    }

    @Override
    public CronJob createOrReplaceCronJob(String path) throws FileNotFoundException {
        InputStream yamlInputStream = byPath(path);
        try {
            CronJob cronJob = KubernetesUtils.client.batch().cronjobs().load(yamlInputStream).get();
            String nameSpace = cronJob.getMetadata().getNamespace();
            cronJob = KubernetesUtils.client.batch().cronjobs().inNamespace(nameSpace).createOrReplace(cronJob);
            return cronJob;
        }catch(Exception e){
            System.out.println("创建CronJob失败，缺少必要的命名空间参数，或是已经有相同的资源对象，在CronJobsServiceImpl类的createOrReplaceCronJob方法");
        }
        return null;
    }

    @Override
    public Pair<Integer, CronJob> getCronJobByNameAndNamespace(String name, String namespace) {
        try{
            CronJob cronJob = KubernetesUtils.client.batch().cronjobs().inNamespace(namespace).withName(name).get();
            return Pair.of(1200, cronJob);
        }catch (Exception e){
            System.out.println("查找CronJob失败，在CronJobsServiceImpl类的getCronJobByNameAndNamespace方法中");
        }
        return Pair.of(1201, null);
    }

    @Override
    public Pair<Integer, String> getCronJobYamlByNameAndNamespace(String name ,String namespace){
        try{
            CronJob cronJob = KubernetesUtils.client.batch().cronjobs().inNamespace(namespace).withName(name).get();
            return Pair.of(1200, Yaml.dump(cronJob));
        }catch(Exception e){
            System.out.println("获取Yaml失败，在 CronJobsServiceImpl 类的getCronJobYamlByNameAndNamespace方法中");
        }
        return Pair.of(1201, null);
    }

    @Override
    public Pair<Integer, Map> getCronJobResources(String name, String namespace){
        try{
            Pair<Integer, CronJob> pair = this.getCronJobByNameAndNamespace(name, namespace);

            if(pair.getLeft() != 1200) return Pair.of(1201, null);// 操作失败
            else if(pair.getLeft() == 1200 && pair.getRight() == null) return Pair.of(1202, null);// 非法操作
            CronJob cronJob = pair.getRight();

            String cronJobUid = cronJob.getMetadata().getUid();

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
                        System.out.println("CronJobController-getCronJobResources-可能Job没有OwnerReferences及以下选项，在 CronJobsServiceImpl 类的 getCronJobResources 方法中");
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
            Pair<Integer, List<Event>> pairOfEvent = CommonServiceImpl.getEventByInvolvedObjectUid(cronJobUid);

            //放入数据
            Map<String, Object> data = new HashMap<>();
            int flag = 0;//标记哪个数据没获取到
            data.put("cronJob", cronJob);
            if(jobInformationList != null) {
                data.put("jobs", jobInformationList.subList(0, mid));
                data.put("runningJobs", jobInformationList.subList(mid, amount));
            }else {
                data.put("jobs", null);
                data.put("runningJobs", null);
                flag |= (1 << 2)|(1 << 1);
            }
            if(pairOfEvent.getRight() != null) {
                data.put("events", pair.getRight());
            }else{
                data.put("events", null);
                flag |= 1;
            }
            data.put("flag", flag);

            if(flag > 0)
                return Pair.of(1203, data); // 部分数据获取失败
            return Pair.of(1200, data); // 完全获取成功
        }catch (Exception e){
            System.out.println("请求失败，在 CronJobsServiceImpl 类的 getCronJobResources 方法中");
        }
        return Pair.of(1201, null); // 获取失败
    }
}

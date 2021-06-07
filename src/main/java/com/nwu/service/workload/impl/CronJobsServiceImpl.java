package com.nwu.service.workload.impl;

import com.nwu.service.workload.CronJobsService;
import com.nwu.util.KubernetesUtils;
import io.fabric8.kubernetes.api.model.batch.CronJob;
import io.kubernetes.client.util.Yaml;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

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
    public List<CronJob> findAllCronJobs(){
        try{
            List<CronJob> items = KubernetesUtils.client.batch().cronjobs().inAnyNamespace().list().getItems();
            return items;
        }catch(Exception e){
            System.out.println("查找CronJobs失败，在CronJobsServiceImpl类的findAllCronJobs方法中");
        }
        return null;
    }

    @Override
    public List<CronJob> findCronJobsByNamespace(String namespace) {
        try{
            List<CronJob> items = KubernetesUtils.client.batch().cronjobs().inNamespace(namespace).list().getItems();
            return items;
        }catch(Exception e){
            System.out.println("查找CronJobs失败，在CronJobsServiceImpl类的findCronJobsByNamespace方法中");
        }
        return null;
    }

    @Override
    public Boolean deleteCronJobByNameAndNamespace(String name, String namespace){
        try{
            Boolean delete = KubernetesUtils.client.batch().cronjobs().inNamespace(namespace).withName(name).delete();
            return delete;
        }catch(Exception e){
            System.out.println("删除CronJob在失败，CronJobsServiceImpl类的deleteCronJobByNameAndNamespace方法中");
        }
        return null;
    }

    @Override
    public CronJob loadCronJobFromYaml(String path) throws FileNotFoundException {
        InputStream yamlInputStream = byPath(path);
        try{
            CronJob cronJob = KubernetesUtils.client.batch().cronjobs().load(yamlInputStream).get();
            return cronJob;
        }catch(Exception e){
            System.out.println("加载CronJob失败，在CronJobsServiceImpl类的loadCronJobFromYaml方法中");
        }
        return null;
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
    public CronJob getCronJobByNameAndNamespace(String name, String namespace) {
        try{
            CronJob cronJob = KubernetesUtils.client.batch().cronjobs().inNamespace(namespace).withName(name).get();
            return cronJob;
        }catch (Exception e){
            System.out.println("查找CronJob失败，在CronJobsServiceImpl类的getCronJobByNameAndNamespace方法中");
        }
        return null;
    }

    @Override
    public String getCronJobYamlByNameAndNamespace(String name ,String namespace){
        try{
            CronJob cronJob = KubernetesUtils.client.batch().cronjobs().inNamespace(namespace).withName(name).get();
            return Yaml.dump(cronJob);
        }catch(Exception e){
            System.out.println("获取Yaml失败，在CronJobsServiceImpl类的getCronJobYamlByNameAndNamespace方法中");
        }
        return null;
    }
}

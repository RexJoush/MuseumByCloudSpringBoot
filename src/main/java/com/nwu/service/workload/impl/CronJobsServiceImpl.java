package com.nwu.service.workload.impl;

import com.nwu.service.workload.CronJobsService;
import com.nwu.util.KubernetesConfig;
import io.fabric8.kubernetes.api.model.batch.CronJob;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import static com.nwu.service.getYamlInputStream.byPath;

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

        List<CronJob> items = KubernetesConfig.client.batch().cronjobs().inAnyNamespace().list().getItems();

        return items;

    }

    @Override
    public List<CronJob> findCronJobsByNamespace(String namespace) {

        List<CronJob> items = KubernetesConfig.client.batch().cronjobs().inNamespace(namespace).list().getItems();

        return items;
    }

    @Override
    public Boolean deleteCronJobByNameAndNamespace(String name, String namespace){

        Boolean delete = KubernetesConfig.client.batch().cronjobs().inNamespace(namespace).withName(name).delete();

        return delete;
    }

    @Override
    public CronJob loadCronJobFromYaml(String path) throws FileNotFoundException {

        InputStream yamlInputStream = byPath(path);

        CronJob cronJob = KubernetesConfig.client.batch().cronjobs().load(yamlInputStream).get();

        return cronJob;
    }

    @Override
    public CronJob createCronJobByYaml(String path) throws FileNotFoundException {

        InputStream yamlInputStream = byPath(path);

        CronJob cronJob = KubernetesConfig.client.batch().cronjobs().load(yamlInputStream).get();
        String nameSpace = cronJob.getMetadata().getNamespace();
        try {
            cronJob = KubernetesConfig.client.batch().cronjobs().inNamespace(nameSpace).create(cronJob);
        }catch(Exception e){
            System.out.println("缺少必要的命名空间参数，或是已经有相同的资源对象，在CronJobsServiceImpl类的createCronJobByYaml方法");
        }
        return cronJob;
    }

    @Override
    public CronJob createOrReplaceCronJob(String path) throws FileNotFoundException {

        InputStream yamlInputStream = byPath(path);

        CronJob cronJob = KubernetesConfig.client.batch().cronjobs().load(yamlInputStream).get();
        String nameSpace = cronJob.getMetadata().getNamespace();

        try {
            cronJob = KubernetesConfig.client.batch().cronjobs().inNamespace(nameSpace).createOrReplace(cronJob);
        }catch(Exception e){
            System.out.println("缺少必要的命名空间参数，或是已经有相同的资源对象，在CronJobsServiceImpl类的createOrReplaceCronJob方法");
        }
        return cronJob;
    }
}

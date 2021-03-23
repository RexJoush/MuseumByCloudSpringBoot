package com.nwu.service.workload.impl;

import com.nwu.service.workload.JobsService;
import com.nwu.util.KubernetesConfig;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.batch.CronJob;
import io.fabric8.kubernetes.api.model.batch.Job;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;

/**
 * @author Rex Joush
 * @time 2021.03.22
 */

/**
 * Jobs 的 service 层实现类
 */
@Service
public class JobsServiceImpl implements JobsService {
    @Override
    public List<Job> findAllJobs(){

        List<Job> items = KubernetesConfig.client.batch().jobs().inAnyNamespace().list().getItems();

        return items;

    }

    @Override
    public List<Job> findJobsByNamespace(String namespace) {

        List<Job> items = KubernetesConfig.client.batch().jobs().inNamespace(namespace).list().getItems();

        return items;
    }

    @Override
    public Boolean deleteJobByNameAndNamespace(String name, String namespace){

        Boolean delete = KubernetesConfig.client.batch().jobs().inNamespace(namespace).withName(name).delete();

        return delete;
    }

    @Override
    public Job loadJobFromYaml(InputStream yamlInputStream){

        Job job = KubernetesConfig.client.batch().jobs().load(yamlInputStream).get();

        return job;
    }

    @Override
    public Job createJobByYaml(InputStream yamlInputStream){

        Job job = KubernetesConfig.client.batch().jobs().load(yamlInputStream).get();
        String nameSpace = job.getMetadata().getNamespace();
        try {
            job = KubernetesConfig.client.batch().jobs().inNamespace(nameSpace).create(job);
        }catch(Exception e){
            System.out.println("缺少必要的命名空间参数，或是已经有相同的资源对象，在JobsServiceImpl类的createJobByYaml方法");
        }
        return job;
    }

    @Override
    public Job createOrReplaceJob(InputStream yamlInputStream){

        Job job = KubernetesConfig.client.batch().jobs().load(yamlInputStream).get();
        String nameSpace = job.getMetadata().getNamespace();

        try {
            job = KubernetesConfig.client.batch().jobs().inNamespace(nameSpace).createOrReplace(job);
        }catch(Exception e){
            System.out.println("缺少必要的命名空间参数，或是已经有相同的资源对象，在JobsServiceImpl类的createOrReplaceJob方法");
        }
        return job;
    }
}

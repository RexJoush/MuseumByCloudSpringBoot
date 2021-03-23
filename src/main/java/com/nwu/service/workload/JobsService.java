package com.nwu.service.workload;

/**
 * @author Rex Joush
 * @time 2021.03.22
 */

import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.batch.CronJob;
import io.fabric8.kubernetes.api.model.batch.Job;
import io.kubernetes.client.openapi.ApiException;

import java.io.InputStream;
import java.util.List;

/**
 * Jobs 的 service 层接口
 */
public interface JobsService {
    /**
     * 获取 Job 列表
     * @return Job 列表
     */
    List<Job> findAllJobs() throws ApiException;


    /**
     * 通过 namespace 获取 Job 列表
     * @param namespace namespace 名称
     * @return Job 列表
     */
    List<Job> findJobsByNamespace(String namespace);

    /**
     * 通过名称删除Job
     * @param name Job名称
     * @param namespace Job所在命名空间名称默认为“default”
     * @return 删除结果 bool型
     */
    Boolean deleteJobByNameAndNamespace(String name, String namespace);

    /**
     * 从yaml文件加载一个job到Job实例
     * @param yamlInputStream yaml文件输入流 InputStream
     * @return 加载的Job
     */
    Job loadJobFromYaml(InputStream yamlInputStream);

    /**
     * 通过yaml文件创建Job
     * @param yamlInputStream yaml文件输入流 InputStream
     * @return 创建的job
     */
    Job createJobByYaml(InputStream yamlInputStream);
}

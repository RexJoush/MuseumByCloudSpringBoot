package com.nwu.service.workload;

/**
 * @author Rex Joush
 * @time 2021.03.22
 */

import io.fabric8.kubernetes.api.model.apps.DaemonSet;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.batch.CronJob;
import io.fabric8.kubernetes.api.model.batch.Job;
import io.kubernetes.client.openapi.ApiException;

import java.io.FileNotFoundException;
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
     * @param path yaml文件输入路径 String
     * @return 加载的Job
     */
    Job loadJobFromYaml(String path) throws FileNotFoundException;

    /**
     * 通过yaml文件创建Job
     * @param path yaml文件输入路径 String
     * @return 创建的job
     */
    Job createJobByYaml(String path) throws FileNotFoundException;

    /**
     * 通过yaml文件创建或更新Job
     * @param path yaml文件输入路径 String
     * @return 创建或更新的job
     */
    Job createOrReplaceJob(String path) throws FileNotFoundException;

    /**
     * 通过名字和命名空间查找 Job
     * @param name Job名字
     * @param namespace Job命名空间
     * @return 查找到的Job
     */
    Job getJobByNameAndNamespace(String name, String namespace);

    /**
     * 通过名字和命名空间获取 Yaml 格式的 Job
     * @param name Job 的名字
     * @param namespace Job 的命名空间
     * @return Yaml 格式的 Job
     */
    String getJobYamlByNameAndNamespace(String name, String namespace);
}

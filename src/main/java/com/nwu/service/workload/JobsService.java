package com.nwu.service.workload;

/**
 * @author Rex Joush
 * @time 2021.03.22
 */

import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.batch.Job;
import io.kubernetes.client.openapi.ApiException;
import org.apache.commons.lang3.tuple.Pair;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Jobs 的 service 层接口
 */
public interface JobsService {
    /**
     * 获取 Job 列表
     * @return Job 列表
     */
    Pair<Integer, List<Job>> findAllJobs() throws ApiException;


    /**
     * 通过 Namespace 获取 Job 列表
     * @param namespace Namespace 名称
     * @return Job 列表和执行代码
     */
    Pair<Integer, List<Job>> findJobsByNamespace(String namespace);

    /**
     * 通过名称删除 Job
     * @param name Job 名称
     * @param namespace Job 所在命名空间名称默认为 “default”
     * @return 删除结果和执行代码
     */
    Pair<Integer, Boolean> deleteJobByNameAndNamespace(String name, String namespace);

    /**
     * 从 Yaml 文件加载一个 Job 到 Job 实例
     * @param yaml Yaml 文件 String 形式
     * @return 加载的 Job 和执行代码
     */
    Pair<Integer, Job> loadJobFromYaml(String yaml) throws FileNotFoundException;

    /**
     * 创建或更新 Job
     * @param yaml 描述 Job 的 Yaml 格式字符串
     * @return 创建结果和执行代码
     */
    Pair<Integer, Boolean> createOrReplaceJobByYamlString(String yaml) throws IOException, ApiException;

    // 弃用
    /**
     * 通过yaml文件创建 Job
     * @param path yaml文件输入路径 String
     * @return 创建的 job
     */
    Job createJobByYaml(String path) throws FileNotFoundException;

    // 弃用
    /**
     * 通过yaml文件创建或更新Job
     * @param path yaml文件输入路径 String
     * @return 创建或更新的job
     */
    Job createOrReplaceJob(String path) throws FileNotFoundException;

    /**
     * 通过名字和命名空间查找 Job
     * @param name Job名字
     * @param namespace Job 命名空间
     * @return 查找到的 Job 和执行代码
     */
    Pair<Integer, Job> getJobByNameAndNamespace(String name, String namespace);

    /**
     * 通过名字和命名空间获取 Yaml 格式的 Job
     * @param name Job 的名字
     * @param namespace Job 的命名空间
     * @return Yaml 格式的 Job 和执行代码
     */
    Pair<Integer, String> getJobYamlByNameAndNamespace(String name, String namespace);

    /**
     * 获取 Job 包含的 Pods
     * @param name Job 名称
     * @param namespace Job 命名空间
     * @return Pods 列表和执行代码
     */
    Pair<Integer, List<Pod>> getPodJobInvolved(String name, String namespace);

    /**
     * 获取 Job 有关的资源
     * @param name 名称
     * @param namespace 命名空间
     * @return 相关资源和执行代码
     */
    Pair<Integer, Map> getJobResources(String name, String namespace);
}

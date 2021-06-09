package com.nwu.service.workload;

/**
 * @author Rex Joush
 * @time 2021.03.22
 */

import io.fabric8.kubernetes.api.model.batch.CronJob;
import io.kubernetes.client.openapi.ApiException;
import org.apache.commons.lang3.tuple.Pair;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

/**
 * Cron Jobs 的 service 层接口
 */
public interface CronJobsService {
    /**
     * 获取 CronJob 列表
     * @return CronJob 列表和执行代码
     */
    Pair<Integer, List<CronJob>> findAllCronJobs() throws ApiException;


    /**
     * 通过 namespace 获取 CronJob 列表
     * @param namespace namespace 名称
     * @return CronJob 列表和执行代码
     */
    Pair<Integer, List<CronJob>> findCronJobsByNamespace(String namespace);

    /**
     * 通过名称删除 CronJob
     * @param name CronJob 名称
     * @param namespace CronJob 所在命名空间名称默认为 “default”
     * @return 删除结果和执行代码
     */
    Pair<Integer, Boolean> deleteCronJobByNameAndNamespace(String name, String namespace);

    /**
     * 从 Yaml 文件加载一个 Cronjob 到 CronJob 实例
     * @param path yaml文件输入路径 String
     * @return 加载的 CronJob 和执行代码
     */
    Pair<Integer, CronJob> loadCronJobFromYaml(String path) throws FileNotFoundException;

    // 弃用
    /**
     * 通过 Yaml 文件创建 CronJob
     * @param path Yaml文件输入路径 String
     * @return 创建的 Cronjob 和执行代码
     */
    CronJob createCronJobByYaml(String path) throws FileNotFoundException;

    /**
     * 创建或更新 CronJob
     * @param yaml 描述 CronJob 的 Yaml 格式字符串
     * @return 创建结果和执行代码
     */
    Pair<Integer, Boolean> createOrReplaceCronJobByYamlString(String yaml);

    // 弃用
    /**
     * 通过 Yaml文件创建或更新 CronJob
     * @param path Yaml文件输入路径 String
     * @return 创建或更新的 Cronjob
     */
    CronJob createOrReplaceCronJob(String path) throws FileNotFoundException;

    /**
     * 通过名字和命名空间查找 CronJob
     * @param name CronJob名字
     * @param namespace CronJob 命名空间
     * @return 查找到的 CronJob
     */
    Pair<Integer, CronJob> getCronJobByNameAndNamespace(String name, String namespace);

    /**
     * 通过名字和命名空间获取 Yaml 格式的 CronJob
     * @param name CronJob 的名字
     * @param namespace CronJob 的命名空间
     * @return Yaml 格式的 CronJob
     */
    Pair<Integer, String> getCronJobYamlByNameAndNamespace(String name, String namespace);

    /**
     * 获取 CronJob 有关的资源
     * @param name 名称
     * @param namespace 命名空间
     * @return 相关资源和执行代码
     */
    Pair<Integer, Map> getCronJobResources(String name, String namespace);
}

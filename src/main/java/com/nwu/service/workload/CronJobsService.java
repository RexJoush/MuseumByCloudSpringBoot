package com.nwu.service.workload;

/**
 * @author Rex Joush
 * @time 2021.03.22
 */

import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.batch.CronJob;
import io.kubernetes.client.openapi.ApiException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

/**
 * Cron Jobs 的 service 层接口
 */
public interface CronJobsService {
    /**
     * 获取 CronJob 列表
     * @return CronJob 列表
     */
    List<CronJob> findAllCronJobs() throws ApiException;


    /**
     * 通过 namespace 获取 CronJob 列表
     * @param namespace namespace 名称
     * @return CronJob 列表
     */
    List<CronJob> findCronJobsByNamespace(String namespace);

    /**
     * 通过名称删除CronJob
     * @param name CronJob名称
     * @param namespace CronJob所在命名空间名称默认为“default”
     * @return 删除结果 bool型
     */
    Boolean deleteCronJobByNameAndNamespace(String name, String namespace);

    /**
     * 从yaml文件加载一个Cronjob到CronJob实例
     * @param path yaml文件输入路径 String
     * @return 加载的CronJob
     */
    CronJob loadCronJobFromYaml(String path) throws FileNotFoundException;

    /**
     * 通过yaml文件创建CronJob
     * @param path yaml文件输入路径 String
     * @return 创建的Cronjob
     */
    CronJob createCronJobByYaml(String path) throws FileNotFoundException;

    /**
     * 通过yaml文件创建或更新CronJob
     * @param path yaml文件输入路径 String
     * @return 创建或更新的Cronjob
     */
    CronJob createOrReplaceCronJob(String path) throws FileNotFoundException;

    /**
     * 通过名字和命名空间查找CronJob
     * @param name CronJob名字
     * @param namespace CronJob命名空间
     * @return 查找到的CronJob
     */
    CronJob getCronJobByNameAndNamespace(String name, String namespace);

    /**
     * 通过名字和命名空间获取 Yaml 格式的 CronJob
     * @param name CronJob 的名字
     * @param namespace CronJob 的命名空间
     * @return Yaml 格式的 CronJob
     */
    String getCronJobYamlByNameAndNamespace(String name, String namespace);
}

package com.nwu.service.workload;

/**
 * @author Rex Joush
 * @time 2021.03.22
 */

import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.batch.CronJob;
import io.kubernetes.client.openapi.ApiException;

import java.io.FileInputStream;
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
     * @param yamlInputStream yaml文件输入流 InputStream
     * @return 加载的CronJob
     */
    CronJob loadCronJobFromYaml(InputStream yamlInputStream);

    /**
     * 通过yaml文件创建CronJob
     * @param yamlInputStream yaml文件输入流 InputStream
     * @return 创建的Cronjob
     */
    CronJob createCronJobByYaml(InputStream yamlInputStream);

    /**
     * 通过yaml文件创建或更新CronJob
     * @param yamlInputStream yaml文件输入流 InputStream
     * @return 创建或更新的Cronjob
     */
    CronJob createOrReplaceCronJob(InputStream yamlInputStream);
}

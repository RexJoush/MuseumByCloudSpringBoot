package com.nwu.service.workload;

import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.batch.CronJob;
import io.kubernetes.client.openapi.ApiException;

import java.io.InputStream;
import java.util.List;

/**
 * @author Rex Joush
 * @time 2021.03.22
 */

/**
 *  Pods 的 service 层接口
 */
public interface PodsService {

    /**
     * 获取 Pod 列表
     * @return pod 列表
     */
    List<Pod> findAllPods() throws ApiException;


    /**
     * 通过 namespace 获取 pod 列表
     * @param namespace namespace 名称
     * @return pod 列表
     */
    List<Pod> findPodsByNamespace(String namespace);

    /**
     * 通过名称删除Pod
     * @param name Pod名称
     * @param namespace Pod所在命名空间名称默认为“default”
     * @return 删除结果 bool型
     */
    Boolean deletePodByNameAndNamespace(String name, String namespace);

    /**
     * 从yaml文件加载一个Pod到Pod实例
     * @param yamlInputStream yaml文件输入流 InputStream
     * @return 加载的Pod
     */
    Pod loadPodFromYaml(InputStream yamlInputStream);

    /**
     * 通过yaml文件创建CronJob
     * @param yamlInputStream yaml文件输入流 InputStream
     * @return 创建的Cronjob
     */
    Pod createPodByYaml(InputStream yamlInputStream);
}

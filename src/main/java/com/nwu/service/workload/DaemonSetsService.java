package com.nwu.service.workload;

/**
 * @author Rex Joush
 * @time 2021.03.22
 */

import io.fabric8.kubernetes.api.model.apps.DaemonSet;
import io.fabric8.kubernetes.api.model.batch.CronJob;
import io.kubernetes.client.openapi.ApiException;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

/**
 * Daemon Sets 的 service 层接口
 */
public interface DaemonSetsService {
    /**
     * 获取 DaemonSet 列表
     * @return DaemonSet 列表
     */
    List<DaemonSet> findAllDaemonSets() throws ApiException;


    /**
     * 通过 namespace 获取 DaemonSet 列表
     * @param namespace namespace 名称
     * @return DaemonSet 列表
     */
    List<DaemonSet> findDaemonSetsByNamespace(String namespace);

    /**
     * 通过名称删除 DaemonSet
     * @param name DaemonSet名称
     * @param namespace DaemonSet所在命名空间名称默认为“default”
     * @return 删除结果 bool型
     */
    Boolean deleteDaemonSetByNameAndNamespace(String name, String namespace);

    /**
     * 从yaml文件加载一个DaemonSet到DaemonSet实例
     * @param path yaml文件输入路径 String
     * @return 加载的DaemonSet
     */
    DaemonSet loadDaemonSetFromYaml(String path) throws FileNotFoundException;

    /**
     * 通过yaml文件创建DaemonSet
     * @param path yaml文件输入路径 String
     * @return 创建的DaemonSet
     */
    DaemonSet createDaemonSetByYaml(String path) throws FileNotFoundException;

    /**
     * 通过yaml文件创建或更新DaemonSet
     * @param path yaml文件输入路径 String
     * @return 创建或更新的DaemonSet
     */
    DaemonSet createOrReplaceDaemonSet(String path) throws FileNotFoundException;
}

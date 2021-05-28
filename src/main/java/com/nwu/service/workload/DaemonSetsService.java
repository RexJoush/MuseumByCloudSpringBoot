package com.nwu.service.workload;

/**
 * @author Rex Joush
 * @time 2021.03.22
 */

import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.apps.DaemonSet;
import io.kubernetes.client.openapi.ApiException;

import java.io.FileNotFoundException;
import java.io.IOException;
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
     * 通过yaml创建DaemonSet
     * @param yaml yaml字符串
     * @return 创建的DaemonSet
     */
    Boolean createOrReplaceDaemonSetByYaml(String yaml) throws IOException;

    /**
     * 通过yaml文件创建或更新DaemonSet
     * @param path yaml文件输入路径 String
     * @return 创建或更新的DaemonSet
     */
    DaemonSet createOrReplaceDaemonSet(String path) throws FileNotFoundException;

    /**
     * 通过名字和命名空间查找DaemonSet
     * @param name DaemonSet名字
     * @param namespace DaemonSet命名空间
     * @return 查找到的DaemonSet
     */
    DaemonSet getDaemonSetByNameAndNamespace(String name, String namespace);

    /**
     * 通过名字和命名空间获取 Yaml 格式的 DaemonSet
     * @param name DaemonSet 的名字
     * @param namespace DaemonSet 的命名空间
     * @return Yaml 格式的 DaemonSet
     */
    String getDaemonSetYamlByNameAndNamespace(String name, String namespace);

    /**
     * 获取 DaemonSet 包含的 Pods
     * @param name DaemonSet 名称
     * @param namespace DaemonSet 命名空间
     * @return Pods 列表
     */
    List<Pod> getPodDaemonSetInvolved(String name, String namespace);
}

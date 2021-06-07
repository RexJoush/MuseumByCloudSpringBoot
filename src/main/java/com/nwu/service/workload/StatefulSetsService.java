package com.nwu.service.workload;

/**
 * @author Rex Joush
 * @time 2021.03.22
 */

import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.apps.StatefulSet;
import io.kubernetes.client.openapi.ApiException;

import java.io.FileNotFoundException;
import java.util.List;

/**
 * Stateful Sets 的 service 层接口
 */
public interface StatefulSetsService {
    /**
     * 获取 StatefulSet 列表
     * @return StatefulSet 列表
     */
    List<StatefulSet> findAllStatefulSets() throws ApiException;


    /**
     * 通过 namespace 获取 StatefulSet 列表
     * @param namespace namespace 名称
     * @return StatefulSet 列表
     */
    List<StatefulSet> findStatefulSetsByNamespace(String namespace);

    /**
     * 通过名称删除StatefulSet
     * @param name StatefulSet名称
     * @param namespace StatefulSet所在命名空间名称默认为“default”
     * @return 删除结果 bool型
     */
    Boolean deleteStatefulSetByNameAndNamespace(String name, String namespace);

    /**
     * 从yaml文件加载一个StatefulSet到StatefulSet实例
     * @param path yaml文件输入路径 String
     * @return 加载的StatefulSet
     */
    StatefulSet loadStatefulSetFromYaml(String path) throws FileNotFoundException;

    /**
     * 通过yaml文件创建StatefulSet
     * @param path yaml文件输入路径 String
     * @return 创建的StatefulSet
     */
    StatefulSet createStatefulSetByYaml(String path) throws FileNotFoundException;

    /**
     * 通过yaml文件创建或更新StatefulSet
     * @param path yaml文件输入路径 String
     * @return 创建或更新的StatefulSet
     */
    StatefulSet createOrReplaceStatefulSet(String path) throws FileNotFoundException;

    /**
     * 通过名字和命名空间查找 StatefulSet
     * @param name StatefulSet名字
     * @param namespace StatefulSet命名空间
     * @return 查找到的StatefulSet
     */
    StatefulSet getStatefulSetByNameAndNamespace(String name, String namespace);

    /**
     * 通过名字和命名空间获取 Yaml 格式的 StatefulSet
     * @param name StatefulSet 的名字
     * @param namespace StatefulSet 的命名空间
     * @return Yaml 格式的 StatefulSet
     */
    String getStatefulSetYamlByNameAndNamespace(String name, String namespace);

    /**
     * 获取 StatefulSet 包含的 Pods
     * @param name StatefulSet 名称
     * @param namespace StatefulSet 命名空间
     * @return Pods 列表
     */
    List<Pod> getPodStatefulSetInvolved(String name, String namespace);
}

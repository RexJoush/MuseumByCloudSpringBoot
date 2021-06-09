package com.nwu.service.workload;

/**
 * @author Rex Joush
 * @time 2021.03.22
 */

import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.apps.StatefulSet;
import io.kubernetes.client.openapi.ApiException;
import org.apache.commons.lang3.tuple.Pair;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

/**
 * Stateful Sets 的 service 层接口
 */
public interface StatefulSetsService {
    /**
     * 获取 StatefulSet 列表
     * @return StatefulSet 列表
     */
    Pair<Integer, List<StatefulSet>> findAllStatefulSets() throws ApiException;


    /**
     * 通过 Namespace 获取 StatefulSet 列表
     * @param namespace namespace 名称
     * @return StatefulSet 列表
     */
    Pair<Integer, List<StatefulSet>> findStatefulSetsByNamespace(String namespace);

    /**
     * 通过名称删除 StatefulSet
     * @param name StatefulSet 名称
     * @param namespace StatefulSet 所在命名空间名称默认为“ default”
     * @return 删除结果 bool型
     */
    Pair<Integer, Boolean> deleteStatefulSetByNameAndNamespace(String name, String namespace);

    /**
     * 从 Yaml 文件加载一个 StatefulSet 到 StatefulSet 实例
     * @param path Yaml文件输入路径 String
     * @return 加载的 StatefulSet
     */
    Pair<Integer, StatefulSet> loadStatefulSetFromYaml(String path) throws FileNotFoundException;

    /**
     * 创建或更新 StatefulSet
     * @param yaml 描述 StatefulSet 的 Yaml 格式字符串
     * @return 创建结果代码
     */
    Pair<Integer, Boolean> createOrReplaceStatefulSetByYamlString(String yaml);

    // 弃用
    /**
     * 通过yaml文件创建 StatefulSet
     * @param path yaml文件输入路径 String
     * @return 创建的StatefulSet
     */
    StatefulSet createStatefulSetByYaml(String path) throws FileNotFoundException;

    // 弃用
    /**
     * 通过yaml文件创建或更新StatefulSet
     * @param path yaml文件输入路径 String
     * @return 创建或更新的StatefulSet
     */
    StatefulSet createOrReplaceStatefulSet(String path) throws FileNotFoundException;

    /**
     * 通过名字和命名空间查找 StatefulSet
     * @param name StatefulSet 名字
     * @param namespace StatefulSet 命名空间
     * @return 查找到的 StatefulSet
     */
    Pair<Integer, StatefulSet> getStatefulSetByNameAndNamespace(String name, String namespace);

    /**
     * 通过名字和命名空间获取 Yaml 格式的 StatefulSet
     * @param name StatefulSet 的名字
     * @param namespace StatefulSet 的命名空间
     * @return Yaml 格式的 StatefulSet
     */
    Pair<Integer, String> getStatefulSetYamlByNameAndNamespace(String name, String namespace);

    /**
     * 获取 StatefulSet 包含的 Pods
     * @param name StatefulSet 名称
     * @param namespace StatefulSet 命名空间
     * @return Pods 列表
     */
    Pair<Integer, List<Pod>> getPodStatefulSetInvolved(String name, String namespace);

    /**
     * 获取 StatefulSet 有关的资源
     * @param name 名称
     * @param namespace 命名空间
     * @return 相关资源和执行代码
     */
    Pair<Integer, Map> getStatefulSetResources(String name, String namespace);
}

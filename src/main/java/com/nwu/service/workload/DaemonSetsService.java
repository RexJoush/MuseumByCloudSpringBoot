package com.nwu.service.workload;

/**
 * @author Rex Joush
 * @time 2021.03.22
 */

import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.apps.DaemonSet;
import io.kubernetes.client.openapi.ApiException;
import org.apache.commons.lang3.tuple.Pair;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Daemon Sets 的 service 层接口
 */
public interface DaemonSetsService {
    /**
     * 获取 DaemonSet 列表
     * @return DaemonSet 列表和执行代码
     */
    Pair<Integer, List<DaemonSet>> findAllDaemonSets() throws ApiException;


    /**
     * 通过 Namespace 获取 DaemonSet 列表
     * @param namespace Namespace 名称
     * @return DaemonSet 列表和执行代码
     */
    Pair<Integer, List<DaemonSet>> findDaemonSetsByNamespace(String namespace);

    /**
     * 通过名称删除 DaemonSet
     * @param name DaemonSet名称
     * @param namespace DaemonSet 所在命名空间名称默认为 “default”
     * @return 删除结果和执行代码
     */
    Pair<Integer, Boolean> deleteDaemonSetByNameAndNamespace(String name, String namespace);

    /**
     * 从 Yaml文件加载一个 DaemonSet 到 DaemonSet 实例
     * @param path Yaml文件输入路径 String
     * @return 加载的 DaemonSet 和执行代码
     */
    Pair<Integer, DaemonSet> loadDaemonSetFromYaml(String path) throws FileNotFoundException;

    /**
     * 创建或更新 DaemonSet
     * @param yaml 描述 DaemonSet 的 Yaml 格式字符串
     * @return 创建结果和执行代码
     */
    Pair<Integer, Boolean> createOrReplaceDaemonSetByYamlString(String yaml);

    /**
     * 通过 Yaml 创建 DaemonSet
     * @param yaml Yaml 字符串
     * @return 创建的 DaemonSet 和执行代码
     */
    Boolean createOrReplaceDaemonSetByYaml(String yaml) throws IOException;

    /**
     * 通过 Yaml文件创建或更新 DaemonSet
     * @param path Yaml文件输入路径 String
     * @return 创建或更新的 DaemonSet 和执行代码
     */
    DaemonSet createOrReplaceDaemonSet(String path) throws FileNotFoundException;

    /**
     * 通过名字和命名空间查找 DaemonSet
     * @param name DaemonSet 名字
     * @param namespace DaemonSet 命名空间
     * @return 查找到的 DaemonSet 和执行代码
     */
    Pair<Integer, DaemonSet> getDaemonSetByNameAndNamespace(String name, String namespace);

    /**
     * 通过名字和命名空间获取 Yaml 格式的 DaemonSet
     * @param name DaemonSet 的名字
     * @param namespace DaemonSet 的命名空间
     * @return Yaml 格式的 DaemonSet 和执行代码
     */
    Pair<Integer, String> getDaemonSetYamlByNameAndNamespace(String name, String namespace);

    /**
     * 获取 DaemonSet 包含的 Pods
     * @param name DaemonSet 名称
     * @param namespace DaemonSet 命名空间
     * @return Pods 列表 和执行代码
     */
    Pair<Integer, List<Pod>> getPodDaemonSetInvolved(String name, String namespace);

    /**
     * 获取 DaemonSet 有关的资源
     * @param name 名称
     * @param namespace 命名空间
     * @return 相关资源和执行代码
     */
    Pair<Integer, Map> getDaemonSetResources(String name, String namespace);
}

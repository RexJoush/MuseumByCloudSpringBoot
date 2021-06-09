package com.nwu.service.workload;

/**
 * @author Rex Joush
 * @time 2021.03.22
 */

import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.apps.ReplicaSet;
import io.kubernetes.client.openapi.ApiException;
import org.apache.commons.lang3.tuple.Pair;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

/**
 * Replica Sets 的 service 层接口
 */
public interface ReplicaSetsService {

    /**
     * 获取 ReplicaSet 列表
     * @return ReplicaSet 列表
     */
    Pair<Integer, List<ReplicaSet>> findAllReplicaSets() throws ApiException;


    /**
     * 通过 Namespace 获取 ReplicaSet 列表
     * @param namespace Namespace 名称
     * @return ReplicaSet 列表
     */
    Pair<Integer, List<ReplicaSet>> findReplicaSetsByNamespace(String namespace);

    /**
     * 通过名称删除 ReplicaSet
     * @param name ReplicaSet 名称
     * @param namespace ReplicaSet 所在命名空间名称默认为 “default”
     * @return 删除结果
     */
    Pair<Integer, Boolean> deleteReplicaSetByNameAndNamespace(String name, String namespace);

    /**
     * 从 Yaml 文件加载一个 ReplicaSet 到 ReplicaSet 实例
     * @param path Yaml 文件输入路径 String
     * @return 加载的 ReplicaSet
     */
    Pair<Integer, ReplicaSet> loadReplicaSetFromYaml(String path) throws FileNotFoundException;

    /**
     * 创建或更新 ReplicaSet
     * @param yaml 描述 ReplicaSet 的 Yaml 格式字符串
     * @return 创建结果代码
     */
    Pair<Integer, Boolean> createOrReplaceReplicaSetByYamlString(String yaml);

    // 弃用
    /**
     * 通过yaml文件创建 ReplicaSet
     * @param path yaml文件输入路径 String
     * @return 创建的ReplicaSet
     */
    ReplicaSet createReplicaSetByYaml(String path) throws FileNotFoundException;

    // 弃用
    /**
     * 通过yaml文件创建或更新ReplicaSet
     * @param path yaml文件输入路径 String
     * @return 创建或更新的ReplicaSet
     */
    ReplicaSet createOrReplaceReplicaSet(String path) throws FileNotFoundException;

    /**
     * 设置ReplicaSet控制的Pod副本数量
     * @param name ReplicaSet名称
     * @param namespace ReplicaSet命名空间
     * @param replicas Pod副本数量
     */
    Pair<Integer, Boolean> setReplicas(String name, String namespace, Integer replicas);

    /**
     * 通过名字和命名空间查找 ReplicaSet
     * @param name ReplicaSet 名字
     * @param namespace ReplicaSet 命名空间
     * @return 查找到的 ReplicaSet
     */
    Pair<Integer, ReplicaSet> getReplicaSetByNameAndNamespace(String name, String namespace);

    /**
     * 通过名字和命名空间获取 Yaml 格式的 ReplicaSet
     * @param name ReplicaSet 的名字
     * @param namespace ReplicaSet 的命名空间
     * @return Yaml 格式的 ReplicaSet
     */
    Pair<Integer, String> getReplicaSetYamlByNameAndNamespace(String name, String namespace);

    /**
     * 获取 ReplicaSet 包含的 Pods
     * @param name ReplicaSet 名称
     * @param namespace ReplicaSet 命名空间
     * @return Pods 列表
     */
    Pair<Integer, List<Pod>> getPodReplicaSetInvolved(String name, String namespace);

    /**
     * 获取 ReplicaSet 有关的资源
     * @param name 名称
     * @param namespace 命名空间
     * @return 相关资源和执行代码
     */
    Pair<Integer, Map> getReplicaSetResources(String name, String namespace);
}

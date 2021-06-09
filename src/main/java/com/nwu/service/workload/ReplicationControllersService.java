package com.nwu.service.workload;

/**
 * @author Rex Joush
 * @time 2021.03.22
 */

import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.ReplicationController;
import io.kubernetes.client.openapi.ApiException;
import org.apache.commons.lang3.tuple.Pair;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

/**
 * Replication Controllers 的 service 层接口
 */
public interface ReplicationControllersService {
    /**
     * 获取 ReplicationController 列表
     * @return ReplicationController 列表
     */
    Pair<Integer, List<ReplicationController>> findAllReplicationControllers() throws ApiException;


    /**
     * 通过 Namespace 获取 ReplicationController 列表
     * @param namespace Namespace 名称
     * @return ReplicationController 列表
     */
    Pair<Integer, List<ReplicationController>> findReplicationControllersByNamespace(String namespace);

    /**
     * 通过名称删除 ReplicationController
     * @param name ReplicationController 名称
     * @param namespace ReplicationController 所在命名空间名称默认为 “default”
     * @return 删除结果
     */
    Pair<Integer, Boolean> deleteReplicationControllerByNameAndNamespace(String name, String namespace);

    /**
     * 从 Yaml 文件加载一个 ReplicationController 到 ReplicationController 实例
     * @param path Yaml 文件
     * @return 加载的 ReplicationController
     */
    Pair<Integer, ReplicationController> loadReplicationControllerFromYaml(String path) throws FileNotFoundException;

    /**
     * 创建或更新 ReplicationController
     * @param yaml 描述 ReplicationController 的 Yaml 格式字符串
     * @return 创建结果代码
     */
    Pair<Integer, Boolean> createOrReplaceReplicationControllerByYamlString(String yaml);

    /**
     * 通过yaml文件创建 ReplicationController
     * @param path yaml文件输入路径 String
     * @return 创建的ReplicationController
     */
    ReplicationController createReplicationControllerByYaml(String path) throws FileNotFoundException;

    /**
     * 通过yaml文件创建或更新ReplicationController
     * @param path yaml文件输入路径 String
     * @return 创建或更新的ReplicationController
     */
    ReplicationController createOrReplaceReplicationController(String path) throws FileNotFoundException;

    /**
     * 设置 ReplicationController 控制的  Pod副本数量
     * @param name ReplicationController 名称
     * @param namespace ReplicationController 命名空间
     * @param replicas Pod 副本数量
     */
    Pair<Integer, Boolean> setReplicas(String name, String namespace, Integer replicas);

    /**
     * 通过名字和命名空间查找 ReplicationController
     * @param name ReplicationController 名字
     * @param namespace ReplicationController 命名空间
     * @return 查找到的 ReplicationController
     */
    Pair<Integer, ReplicationController> getReplicationControllerByNameAndNamespace(String name, String namespace);

    /**
     * 通过名字和命名空间获取 Yaml 格式的 ReplicationController
     * @param name ReplicationController 的名字
     * @param namespace ReplicationController 的命名空间
     * @return Yaml 格式的 ReplicationController
     */
    Pair<Integer, String> getReplicationControllerYamlByNameAndNamespace(String name, String namespace);

    /**
     * 获取 ReplicationController 包含的 Pods
     * @param name ReplicationController 名称
     * @param namespace ReplicationController 命名空间
     * @return Pods 列表
     */
    Pair<Integer, List<Pod>> getPodReplicationControllerInvolved(String name, String namespace);

    /**
     * 获取 ReplicationController 有关的资源
     * @param name 名称
     * @param namespace 命名空间
     * @return 相关资源和执行代码
     */
    Pair<Integer, Map> getReplicationControllerResources(String name, String namespace);
}

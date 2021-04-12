package com.nwu.service.workload;

/**
 * @author Rex Joush
 * @time 2021.03.22
 */

import io.fabric8.kubernetes.api.model.ReplicationController;
import io.fabric8.kubernetes.api.model.apps.ReplicaSet;
import io.fabric8.kubernetes.api.model.batch.CronJob;
import io.kubernetes.client.openapi.ApiException;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

/**
 * Replication Controllers 的 service 层接口
 */
public interface ReplicationControllersService {
    /**
     * 获取 ReplicationController 列表
     * @return ReplicationController 列表
     */
    List<ReplicationController> findAllReplicationControllers() throws ApiException;


    /**
     * 通过 namespace 获取 ReplicationController 列表
     * @param namespace namespace 名称
     * @return ReplicationController 列表
     */
    List<ReplicationController> findReplicationControllersByNamespace(String namespace);

    /**
     * 通过名称删除ReplicationController
     * @param name ReplicationController名称
     * @param namespace ReplicationController所在命名空间名称默认为“default”
     * @return 删除结果 bool型
     */
    Boolean deleteReplicationControllerByNameAndNamespace(String name, String namespace);

    /**
     * 从yaml文件加载一个ReplicationController到ReplicationController实例
     * @param      * @param path yaml文件输入路径 String yaml文件输入流 InputStream
     * @return 加载的ReplicationController
     */
    ReplicationController loadReplicationControllerFromYaml(String path) throws FileNotFoundException;

    /**
     * 通过yaml文件创建ReplicationController
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
     * 设置ReplicationController控制的Pod副本数量
     * @param name ReplicationController名称
     * @param namespace ReplicationController命名空间
     * @param replicas Pod副本数量
     */
    void setReplicas(String name, String namespace, Integer replicas);

    /**
     * 通过名字和命名空间查找 ReplicationController
     * @param name ReplicationController名字
     * @param namespace ReplicationController命名空间
     * @return 查找到的ReplicationController
     */
    ReplicationController getReplicationControllerByNameAndNamespace(String name, String namespace);

    /**
     * 通过名字和命名空间获取 Yaml 格式的 ReplicationController
     * @param name ReplicationController 的名字
     * @param namespace ReplicationController 的命名空间
     * @return Yaml 格式的 ReplicationController
     */
    String getReplicationControllerYamlByNameAndNamespace(String name, String namespace);
}

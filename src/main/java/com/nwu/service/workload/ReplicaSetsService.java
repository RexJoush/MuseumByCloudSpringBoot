package com.nwu.service.workload;

/**
 * @author Rex Joush
 * @time 2021.03.22
 */

import io.fabric8.kubernetes.api.model.apps.ReplicaSet;
import io.fabric8.kubernetes.api.model.batch.CronJob;
import io.kubernetes.client.openapi.ApiException;

import java.io.InputStream;
import java.util.List;

/**
 * Replica Sets 的 service 层接口
 */
public interface ReplicaSetsService {

    /**
     * 获取 ReplicaSet 列表
     * @return ReplicaSet 列表
     */
    List<ReplicaSet> findAllReplicaSets() throws ApiException;


    /**
     * 通过 namespace 获取 ReplicaSet 列表
     * @param namespace namespace 名称
     * @return ReplicaSet 列表
     */
    List<ReplicaSet> findReplicaSetsByNamespace(String namespace);

    /**
     * 通过名称删除ReplicaSet
     * @param name ReplicaSet名称
     * @param namespace ReplicaSet所在命名空间名称默认为“default”
     * @return 删除结果 bool型
     */
    Boolean deleteReplicaSetByNameAndNamespace(String name, String namespace);

    /**
     * 从yaml文件加载一个ReplicaSet到ReplicaSet实例
     * @param yamlInputStream yaml文件输入流 InputStream
     * @return 加载的ReplicaSet
     */
    ReplicaSet loadReplicaSetFromYaml(InputStream yamlInputStream);

    /**
     * 通过yaml文件创建ReplicaSet
     * @param yamlInputStream yaml文件输入流 InputStream
     * @return 创建的ReplicaSet
     */
    ReplicaSet createReplicaSetByYaml(InputStream yamlInputStream);

    /**
     * 通过yaml文件创建或更新ReplicaSet
     * @param yamlInputStream yaml文件输入流 InputStream
     * @return 创建或更新的ReplicaSet
     */
    ReplicaSet createOrReplaceReplicaSet(InputStream yamlInputStream);

}

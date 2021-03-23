package com.nwu.service.workload;

/**
 * @author Rex Joush
 * @time 2021.03.22
 */

import io.fabric8.kubernetes.api.model.ReplicationController;
import io.fabric8.kubernetes.api.model.batch.CronJob;
import io.kubernetes.client.openapi.ApiException;

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
     * @param yamlInputStream yaml文件输入流 InputStream
     * @return 加载的ReplicationController
     */
    ReplicationController loadReplicationControllerFromYaml(InputStream yamlInputStream);

    /**
     * 通过yaml文件创建ReplicationController
     * @param yamlInputStream yaml文件输入流 InputStream
     * @return 创建的ReplicationController
     */
    ReplicationController createReplicationControllerByYaml(InputStream yamlInputStream);
}

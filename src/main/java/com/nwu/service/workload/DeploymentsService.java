package com.nwu.service.workload;

/**
 * @author Rex Joush
 * @time 2021.03.22
 */

import io.fabric8.kubernetes.api.model.apps.DaemonSet;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.batch.CronJob;
import io.kubernetes.client.openapi.ApiException;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

/**
 *  Deployments 的 service 层接口
 */
public interface DeploymentsService {
    /**
     * 获取 Deployment 列表
     * @return Deployment 列表
     */
    List<Deployment> findAllDeployments() throws ApiException;


    /**
     * 通过 namespace 获取 Deployment 列表
     * @param namespace namespace 名称
     * @return Deployment 列表
     */
    List<Deployment> findDeploymentsByNamespace(String namespace);


    /**
     *
     * @param name Deployment名称
     * @param namespace Deployment所在命名空间
     * @return 删除结果 bool型
     */
    Boolean deleteDeploymentByNameAndNamespace(String name, String namespace);

    /**
     * 从yaml文件加载一个Deployment到Deployment实例
     * @param path yaml文件输入路径 String
     * @return 加载的Deployment
     */
    Deployment loadDeploymentFromYaml(String path) throws FileNotFoundException;

    /**
     * 通过yaml文件创建Deployment
     * @param path yaml文件输入路径 String
     * @return 创建的Deployment
     */
    Deployment createDeploymentByYaml(String path) throws FileNotFoundException;

    /**
     * 通过yaml文件创建或更新Deployment
     * @param path yaml文件输入路径 String
     * @return 创建或更新的Deployment
     */
    Deployment createOrReplaceDeployment(String path) throws FileNotFoundException;

    /**
     * 通过Deployment name和namespace获取Deployment的日志信息
     * @param name Deployment名称
     * @param namespace Deployment命名空间
     * @return 日志信息
     */
    String getDeploymentLogByNameAndNamespace(String name, String namespace);

    /**
     * 设置Deployment控制的Pod副本数量
     * @param name Deployment名称
     * @param namespace Deployment命名空间
     * @param replicas Pod副本数量
     */
    void setReplicas(String name, String namespace, Integer replicas);

}

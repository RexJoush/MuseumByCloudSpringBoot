package com.nwu.service.workload;

/**
 * @author Rex Joush
 * @time 2021.03.22
 */

import io.fabric8.kubernetes.api.model.apps.DaemonSet;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.batch.CronJob;
import io.kubernetes.client.openapi.ApiException;

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
     * @param yamlInputStream yaml文件输入流 InputStream
     * @return 加载的Deployment
     */
    Deployment loadDeploymentFromYaml(InputStream yamlInputStream);

    /**
     * 通过yaml文件创建Deployment
     * @param yamlInputStream yaml文件输入流 InputStream
     * @return 创建的Deployment
     */
    Deployment createDeploymentByYaml(InputStream yamlInputStream);

    /**
     * 通过yaml文件创建或更新Deployment
     * @param yamlInputStream yaml文件输入流 InputStream
     * @return 创建或更新的Deployment
     */
    Deployment createOrReplaceDeployment(InputStream yamlInputStream);

}

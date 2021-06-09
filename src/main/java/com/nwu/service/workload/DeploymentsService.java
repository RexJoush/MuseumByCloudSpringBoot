package com.nwu.service.workload;

/**
 * @author Rex Joush
 * @time 2021.03.22
 */

import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.kubernetes.client.openapi.ApiException;
import org.apache.commons.lang3.tuple.Pair;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

/**
 *  Deployments 的 service 层接口
 */
public interface DeploymentsService {
    /**
     * 获取 Deployment 列表
     * @return Deployment 列表和执行代码
     */
    Pair<Integer, List<Deployment>> findAllDeployments() throws ApiException;


    /**
     * 通过 Namespace 获取 Deployment 列表
     * @param namespace Namespace 名称
     * @return Deployment 列表和执行代码
     */
    Pair<Integer, List<Deployment>> findDeploymentsByNamespace(String namespace);

    /**
     * 通过 Name 和 Namespace 获取 Deployments
     * @param name 名字
     * @param namespace 命名空间
     * @return Deployment 和执行代码
     */
    Pair<Integer, Deployment> getDeploymentByNameAndNamespace(String name, String namespace);


    /**
     *
     * @param name Deployment 名称
     * @param namespace Deployment 所在命名空间
     * @return 删除结果和执行代码
     */
    Pair<Integer, Boolean> deleteDeploymentByNameAndNamespace(String name, String namespace);

    /**
     * 从yaml文件加载一个Deployment到Deployment实例
     * @param path yaml文件输入路径 String
     * @return 加载的Deployment 和执行代码
     */
    Pair<Integer, Deployment> loadDeploymentFromYaml(String path) throws FileNotFoundException;

    /**
     * 通过 Yaml 文件创建 Deployment
     * @param path Yaml 文件输入路径 String
     * @return 创建的Deployment
     */
    Deployment createOrReplaceDeploymentByPath(String path) throws FileNotFoundException;

    /**
     * 通过 Yaml 文件创建或更新 Deployment
     * @param file Yaml文件
     * @return 创建或更新的 Deployment
     */
    Deployment createOrReplaceDeploymentByFile(File file) throws FileNotFoundException, ApiException;

    /**
     * 创建 Deployment
     * @param yaml 描述 Deployment 的 Yaml 字符串
     * @return 创建的结果和执行代码
     */
    Pair<Integer, Boolean> createOrReplaceDeploymentByYamlString(String yaml);

    //弃用
//    /**
//     * 通过Deployment name和namespace获取Deployment的日志信息
//     * @param name Deployment名称
//     * @param namespace Deployment命名空间
//     * @return 日志信息
//     */
//    String getDeploymentLogByNameAndNamespace(String name, String namespace);

    /**
     * 设置 Deployment 控制的 Pod 副本数量
     * @param name Deployment名称
     * @param namespace Deployment 命名空间
     * @param replicas Pod 副本数量
     * @return 结果和执行代码
     */
    Pair<Integer, Boolean> setReplicas(String name, String namespace, Integer replicas);

    /**
     * 通过名字和命名空间获取 Yaml 格式的 Deployment
     * @param name Deployment 的名字
     * @param namespace Deployment 的命名空间
     * @return Yaml 格式的 Deployment 和执行代码
     */
    Pair<Integer, String> getDeploymentYamlByNameAndNamespace(String name, String namespace) throws ApiException;

    /**
     * 获取 Deployment 有关的资源
     * @param name 名称
     * @param namespace 命名空间
     * @return 相关资源和执行代码
     */
    Pair<Integer, Map> getDeploymentResources(String name, String namespace);
}

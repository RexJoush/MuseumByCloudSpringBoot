package com.nwu.service.workload;

import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.batch.CronJob;
import io.kubernetes.client.openapi.ApiException;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * @author Rex Joush
 * @time 2021.03.22
 */

/**
 *  Pods 的 service 层接口
 */
public interface PodsService {

    /**
     * 获取 Pod 列表
     * @return pod 列表
     */
    List<Pod> findAllPods() throws ApiException;


    /**
     * 通过 namespace 获取 pod 列表
     * @param namespace namespace 名称
     * @return pod 列表
     */
    List<Pod> findPodsByNamespace(String namespace);

    /**
     * 通过名称删除Pod
     * @param name Pod名称
     * @param namespace Pod所在命名空间名称默认为“default”
     * @return 删除结果 bool型
     */
    Boolean deletePodByNameAndNamespace(String name, String namespace);

    /**
     * 从yaml文件加载一个Pod到Pod实例
     * @param path yaml文件输入路径 String
     * @return 加载的Pod
     */
    Pod loadPodFromYaml(String path) throws FileNotFoundException;

    /**
     * 通过yaml文件创建Pod
     * @param path yaml文件输入路径 String
     * @return 创建的Pod
     */
    Pod createPodByYaml(String path) throws FileNotFoundException;

    /**
     * 通过yaml文件创建或更新Pod
     * @param path yaml文件输入路径 String
     * @return 创建或更新的Pod
     */
    Pod createOrReplacePod(String path) throws FileNotFoundException;

    /**
     * 通过Pod name和namespace获取Pod的日志信息
     * @param name Pod名称
     * @param namespace Pod命名空间
     * @return
     */
    String getPodLogByNameAndNamespace(String name, String namespace);

    /**
     *从表单创建Pod
     * @param name Pod名称 String
     * @param namespace Pod命名空间 String
     * @param labels Pod标签 Map<String, String>
     * @param annotations Pod描述信息 Map<String, String>
     * @param secretName 下载Pod所用镜像需要的secret名称 String
     * @param images Pod里容器镜像 String
     * @param imagePullPolicy Pod里容器镜像下载策略 String
     * @param command Pod里容器启动后执行的命令列表 String[]
     * @param args Pod里容器的启动命令参数列表 String[]
     * @param cpuLimit Pod里容器Cpu的限制，单位为core数 String
     * @param cpuRequest Pod里容器Cpu请求，容器启动的初始可用数量 String
     * @param memoryLimit Pod里容内存限制，单位可以为Mib/Gib String
     * @param memoryRequest Pod里容器内存请求,容器启动的初始可用数量 String
     * @param envVar Pod里容器环境变量名称和值 Map<String, String>
     * @param amount Pod副本数量 Integer
     * @return 生成的Pod列表
     */
    List<Pod> createPod(String name, String namespace, Map<String, String> labels, Map<String, String> annotations,
                        String secretName, String images, String imagePullPolicy, String[] command, String[] args,
                        String cpuLimit, String cpuRequest, String memoryLimit, String memoryRequest, Map<String, String> envVar, Integer amount);
}

package com.nwu.service.workload;

import com.nwu.entity.workload.PodDefinition;
import com.nwu.entity.workload.PodDetails;
import com.nwu.entity.workload.PodUsage;
import io.fabric8.kubernetes.api.model.Pod;
import io.kubernetes.client.openapi.ApiException;

import java.io.FileNotFoundException;
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
    List<PodDefinition> findAllPods() throws ApiException;


    /**
     * 通过 namespace 获取 pod 列表
     * @param namespace namespace 名称
     * @return pod 列表
     */
    List<PodDefinition> findPodsByNamespace(String namespace);

    /**
     * 通过名字和命名空间查找 Pod
     * @param name Pod名字
     * @param namespace Pod命名空间
     * @return 查找到的Pod
     */
    PodDetails findPodByNameAndNamespace(String name, String namespace);

    /**
     * 根据 node 获取当前节点下的 pod 列表
     * @param nodeName 当前节点名称
     * @return pod 列表
     */
    List<PodDefinition> findPodsByNode(String nodeName);

    /**
     * 保存 pod 节点的资源利用率信息
     */
    void savePodUsage() throws InterruptedException;

    /**
     * 删除 pod 节点两天前的资源利用率信息
     */
    void deletePodUsage() throws InterruptedException;

    /**
     * 获取当前 pod 的近 20 分钟的利用率数据
     * @param podName pod 名称
     * @param podNamespace pod 命名空间
     * @return 利用率列表
     */
    List<PodUsage> findRecentTwenty(String podName, String podNamespace);

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
     * @return 日志信息
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
     * @param memoryLimit Pod里容内存限制，单位可能为Mib/Gib或者Mi/Gi  String
     * @param memoryRequest Pod里容器内存请求,容器启动的初始可用数量 String
     * @param envVar Pod里容器环境变量名称和值 Map<String, String>
     * @param amount Pod副本数量 Integer
     * @return 生成的Pod列表
     */
    List<Pod> createPodFromForm(String name, String namespace, Map<String, String> labels, Map<String, String> annotations,
                        String secretName, String images, String imagePullPolicy, String[] command, String[] args,
                        String cpuLimit, String cpuRequest, String memoryLimit, String memoryRequest, Map<String, String> envVar, Integer amount);
}

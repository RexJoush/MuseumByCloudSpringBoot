package com.nwu.service.workload;

import com.nwu.entity.workload.PodDefinition;
import com.nwu.entity.workload.PodDetails;
import com.nwu.entity.workload.PodForm;
import com.nwu.entity.workload.PodUsage;
import io.fabric8.kubernetes.api.model.Pod;
import io.kubernetes.client.openapi.ApiException;

import java.io.File;
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
     * 获取完整形式的 Pod 列表
     * @return Pod 列表
     */
    List<Pod> findCompletePodsList();

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
     * 通过名字和命名空间查找 Pod 的 yaml 文件
     * @param name Pod名字
     * @param namespace Pod命名空间
     * @return 查找到的 Pod 的 yaml 格式文件
     */
    String findPodYamlByNameAndNamespace(String name, String namespace);

    /**
     * 根据 node 获取当前节点下的 pod 列表
     * @param nodeName 当前节点名称
     * @return pod 列表
     */
    List<PodDefinition> findPodsByNode(String nodeName);

    /**
     * 通过Service中的label来获取相关联的pod
     * @param labelKey 标签名
     * @param labelValue 标签值
     * @return
     */
    List<PodDefinition> findPodBySvcLabel(String labelKey, String labelValue);

    /**
     * 查找被匹配的 Pods
     * @param labels 标签键:标签值
     * @return 返回被匹配的 Pods
     */
    List<Pod> findPodsByLabels(Map<String, String> labels);

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
     * 通过yaml文件路径创建Pod
     * @param path yaml文件输入路径 String
     * @return 创建的Pod
     */
    Pod createPodByYamlPath(String path) throws FileNotFoundException;

    /**
     * 通过 yaml 文件创建 Pod
     * @param yaml File 类型 yaml 文件
     * @return 创建的代码
     */
    int createPodByYamlFile(File yaml) throws FileNotFoundException;

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
     * @param podForm Pod 表单
     * @return 生成的Pod列表
     */
    List<Pod> createPodFromForm(PodForm podForm);
}

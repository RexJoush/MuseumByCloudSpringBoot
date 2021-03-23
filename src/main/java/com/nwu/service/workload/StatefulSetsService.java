package com.nwu.service.workload;

/**
 * @author Rex Joush
 * @time 2021.03.22
 */

import io.fabric8.kubernetes.api.model.apps.StatefulSet;
import io.fabric8.kubernetes.api.model.batch.CronJob;
import io.kubernetes.client.openapi.ApiException;

import java.io.InputStream;
import java.util.List;

/**
 * Stateful Sets 的 service 层接口
 */
public interface StatefulSetsService {
    /**
     * 获取 StatefulSet 列表
     * @return StatefulSet 列表
     */
    List<StatefulSet> findAllStatefulSets() throws ApiException;


    /**
     * 通过 namespace 获取 StatefulSet 列表
     * @param namespace namespace 名称
     * @return StatefulSet 列表
     */
    List<StatefulSet> findStatefulSetsByNamespace(String namespace);

    /**
     * 通过名称删除StatefulSet
     * @param name StatefulSet名称
     * @param namespace StatefulSet所在命名空间名称默认为“default”
     * @return 删除结果 bool型
     */
    Boolean deleteStatefulSetByNameAndNamespace(String name, String namespace);

    /**
     * 从yaml文件加载一个StatefulSet到StatefulSet实例
     * @param yamlInputStream yaml文件输入流 InputStream
     * @return 加载的StatefulSet
     */
    StatefulSet loadStatefulSetFromYaml(InputStream yamlInputStream);

    /**
     * 通过yaml文件创建StatefulSet
     * @param yamlInputStream yaml文件输入流 InputStream
     * @return 创建的StatefulSet
     */
    StatefulSet createStatefulSetByYaml(InputStream yamlInputStream);
}

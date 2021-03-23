package com.nwu.service.settingstorage;

/**
 * @author Rex Joush
 * @time 2021.03.22
 */

import io.fabric8.kubernetes.api.model.ConfigMap;
import io.kubernetes.client.openapi.ApiException;

import java.io.InputStream;
import java.util.List;

/**
 * Config Maps 的 service 层接口
 */
public interface ConfigMapsService {
    /**
     * 获取 ConfigMap 列表
     * @return ConfigMap 列表
     * @throws ApiException
     */
    List<ConfigMap> findAllConfigMaps() throws ApiException;

    /**
     * 通过 namespace 获取 ConfigMaps 列表
     * @param namespace namespace名称
     * @return ConfigMap 列表
     */
    List<ConfigMap> findConfigMapsByNamespace(String namespace);

    /**
     * 通过 name 和 namespace 删除 ConfigMap
     * @param name
     * @param namespace
     * @return
     */
    Boolean deleteConfigMapByNameAndNamespace(String name,String namespace);

    /**
     * 通过加载 yaml 文件
     * @param inputStream
     * @return
     */
    ConfigMap loadConfigMapFromYaml(InputStream inputStream);

    /**
     * 通过 yaml 文件创建 ConfigMap
     * @param inputStream
     * @return
     */
    ConfigMap createConfigMapByYaml(InputStream inputStream);

    /**
     * 修改 ConfigMap 文件
     * @param name
     * @param namespace
     * @return
     */
    //ConfigMap updateConfigMap(String name,String namespace);
}

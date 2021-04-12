package com.nwu.service.settingstorage;

/**
 * @author Rex Joush
 * @time 2021.03.22
 */

import io.fabric8.kubernetes.api.model.ConfigMap;
import io.kubernetes.client.openapi.ApiException;

import java.io.FileNotFoundException;
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
     * @param name CobfigMap 名称
     * @param namespace ConfigMap 所在命名空间名称默认为“default”
     * @return 删除结果 bool型
     */
    Boolean deleteConfigMapByNameAndNamespace(String name,String namespace);

    /**
     * 加载 yaml 文件 到 ConfigMap
     * @param path yaml文件的路径
     * @return 加载的 ConfigMap
     * @throws FileNotFoundException
     */
    ConfigMap loadConfigMapFromYaml(String path) throws FileNotFoundException;

    /**
     * 通过 yaml 文件创建 ConfigMap
     * @param path yaml文件的路径
     * @return 创建的 ConfigMap
     * @throws FileNotFoundException
     */
    ConfigMap createConfigMapByYaml(String path) throws FileNotFoundException;

    /**
     * 通过yaml文件创建或更新ConfigMap
     * @param path yaml文件的路径
     * @return 创建或更新的ConfigMap
     * @throws FileNotFoundException
     */
    ConfigMap createOrReplaceConfigMap(String path) throws FileNotFoundException;

    /**
     * 通过ConfigMap name和namespace获取ConfigMap的信息
     * @param name ConfigMap名称
     * @param namespace ConfigMap命名空间
     * @return ConfigMap信息
     */
    ConfigMap getConfigMapByNameAndNamespace(String name, String namespace);

    /**
     * 通过名字和命名空间查找 ConfigMap 的 yaml 文件
     * @param name ConfigMap 名字
     * @param namespace ConfigMap 命名空间
     * @return 查找到的 ConfigMap 的 yaml 格式文件
     */
    String getConfigMapYamlByNameAndNamespace(String name, String namespace);
}

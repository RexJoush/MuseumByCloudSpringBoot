package com.nwu.service.settingstorage;

/**
 * @author Rex Joush
 * @time 2021.03.22
 */

import io.fabric8.kubernetes.api.model.ConfigMap;
import io.kubernetes.client.openapi.ApiException;

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

}

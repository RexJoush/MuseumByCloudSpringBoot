package com.nwu.service.settingstorage;

/**
 * @author Rex Joush
 * @time 2021.03.22
 */

import io.fabric8.kubernetes.api.model.PersistentVolumeClaim;
import io.fabric8.kubernetes.api.model.Secret;
import io.kubernetes.client.openapi.ApiException;

import java.io.InputStream;
import java.util.List;

/**
 * Persistent Volume Claims 的 service 层接口
 */
public interface PersistentVolumeClaimsService {

    /**
     * 获取 PersistentVolumeClaim 列表
     * @return PersistentVolumeClaim 列表
     * @throws ApiException
     */
    List<PersistentVolumeClaim> findAllPVC() throws ApiException;

    /**
     * 通过 namespace 获取 PersistentVolumeClaim 列表
     * @param namespace namespace名称
     * @return PersistentVolumeClaim 列表
     */
    List<PersistentVolumeClaim> findPVCByNamespace(String namespace);

    /**
     * 通过 name 和 namespace 删除 PersistentVolumeClaim
     * @param name
     * @param namespace
     * @return boolean值
     */
    Boolean deletePVCByNameAndNamespace(String name,String namespace);

    /**
     * 通过加载 yaml 文件
     * @param yamlInputStream
     * @return PersistentVolumeClaim
     */
    PersistentVolumeClaim loadPVCFromYaml(InputStream yamlInputStream);

    /**
     * 通过 yaml 文件创建 secret
     * @param yamlInputStream
     * @return secret
     */
    PersistentVolumeClaim createPVCByYaml(InputStream yamlInputStream);
}

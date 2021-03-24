package com.nwu.service.settingstorage;

/**
 * @author Rex Joush
 * @time 2021.03.22
 */

import io.fabric8.kubernetes.api.model.PersistentVolumeClaim;
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
     * 通过 name 和 namespace 删除 replaceConfigMap
     * @param name PersistentVolumeClaim 名称
     * @param namespace PersistentVolumeClaim 所在命名空间名称默认“default”
     * @return 删除结果bool值
     */
    Boolean deletePVCByNameAndNamespace(String name,String namespace);

    /**
     * 通过加载 yaml 文件到 PersistentVolumeClaim 实例
     * @param yamlInputStream yaml文件输入流 InputStream
     * @return 加载的 PersistentVolumeClaim
     */
    PersistentVolumeClaim loadPVCFromYaml(InputStream yamlInputStream);

    /**
     * 通过 yaml 文件创建 PersistentVolumeClaim
     * @param yamlInputStream yaml文件输入流 InputStream
     * @return 创建的 PersistentVolumeClaim
     */
    PersistentVolumeClaim createPVCByYaml(InputStream yamlInputStream);

    /**
     * 通过yaml文件创建或更新 PersistentVolumeClaim
     * @param yamlInputStream yaml文件输入流 InputStream
     * @return 创建或更新的 PersistentVolumeClaim
     */
    PersistentVolumeClaim createOrReplacePVC(InputStream yamlInputStream);
}

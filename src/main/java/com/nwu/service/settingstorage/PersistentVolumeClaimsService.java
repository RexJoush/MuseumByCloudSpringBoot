package com.nwu.service.settingstorage;

/**
 * @author Rex Joush
 * @time 2021.03.22
 */

import io.fabric8.kubernetes.api.model.PersistentVolumeClaim;
import io.kubernetes.client.openapi.ApiException;

import java.io.FileNotFoundException;
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
     * @param path yaml文件的路径
     * @return 加载的 PersistentVolumeClaim
     * @throws FileNotFoundException
     */
    PersistentVolumeClaim loadPVCFromYaml(String path) throws FileNotFoundException;;

    /**
     * 通过 yaml 文件创建 PersistentVolumeClaim
     * @param path yaml文件输入流 InputStream
     * @return 创建的 PersistentVolumeClaim
     * @throws FileNotFoundException
     */
    PersistentVolumeClaim createPVCByYaml(String path) throws FileNotFoundException;;

    /**
     * 通过yaml文件创建或更新 PersistentVolumeClaim
     * @param path yaml文件输入流 InputStream
     * @return 创建或更新的 PersistentVolumeClaim
     * @throws FileNotFoundException
     */
    PersistentVolumeClaim createOrReplacePVC(String path) throws FileNotFoundException;;

    /**
     * 通过PersistentVolumeClaim name和namespace获取PersistentVolumeClaim的信息
     * @param name PersistentVolumeClaim名称
     * @param namespace PersistentVolumeClaim命名空间
     * @return PersistentVolumeClaim信息
     */
    PersistentVolumeClaim getPVCByNameAndNamespace(String name, String namespace);
}

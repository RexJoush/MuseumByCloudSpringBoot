package com.nwu.service.settingstorage;

/**
 * @author Rex Joush
 * @time 2021.03.22
 */

import io.fabric8.kubernetes.api.model.PersistentVolumeClaim;
import io.kubernetes.client.openapi.ApiException;

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
    List<PersistentVolumeClaim> findAllPersistentVolumeClaims() throws ApiException;

    /**
     * 通过 namespace 获取 PersistentVolumeClaim 列表
     * @param namespace namespace名称
     * @return PersistentVolumeClaim 列表
     */
    List<PersistentVolumeClaim> findPersistentVolumeClaimsByNamespace(String namespace);

}

package com.nwu.service.cluster;

import io.fabric8.kubernetes.api.model.Namespace;
import io.fabric8.kubernetes.api.model.PersistentVolume;

import java.util.List;

/**
 * @author zqy
 * @time 2021.03.24
 */
public interface PersistentVolumesService {

    /**
     * 获取所有 PersistentVolume
     * @return PersistentVolume列表
     */
    List<PersistentVolume> getAllPersistentVolumes();
}

package com.nwu.service.cluster.impl;

import com.nwu.service.cluster.PersistentVolumesService;
import com.nwu.util.KubernetesUtils;
import io.fabric8.kubernetes.api.model.PersistentVolume;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zqy
 * @time 2021.03.24
 */

@Service
public class PersistentVolumesServiceImpl implements PersistentVolumesService {

    @Override
    public List<PersistentVolume> getAllPersistentVolumes(){

        List<PersistentVolume> items = KubernetesUtils.client.persistentVolumes().list().getItems();

        return items;

    }
}

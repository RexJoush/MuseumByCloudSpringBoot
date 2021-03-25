package com.nwu.service.cluster.impl;

import com.nwu.service.cluster.StorageClassesService;
import com.nwu.util.KubernetesUtils;
import io.fabric8.kubernetes.api.model.storage.StorageClass;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zqy
 * @time 2021.03.24
 */

@Service
public class StorageClassesServiceImpl implements StorageClassesService {

    @Override
    public List<StorageClass> getAllStorageClasses(){

        List<StorageClass> items = KubernetesUtils.client.storage().storageClasses().list().getItems();

        return items;

    }
}

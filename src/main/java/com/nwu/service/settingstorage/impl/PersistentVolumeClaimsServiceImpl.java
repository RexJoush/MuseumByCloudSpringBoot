package com.nwu.service.settingstorage.impl;

import com.nwu.service.settingstorage.PersistentVolumeClaimsService;
import com.nwu.util.KubernetesConfig;
import io.fabric8.kubernetes.api.model.PersistentVolumeClaim;
import io.fabric8.kubernetes.api.model.Secret;
import io.kubernetes.client.openapi.ApiException;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;

/**
 * @author Rex Joush
 * @time 2021.03.22
 */
@Service
public class PersistentVolumeClaimsServiceImpl implements PersistentVolumeClaimsService {


    @Override
    public List<PersistentVolumeClaim> findAllPVC() throws ApiException {

        List<PersistentVolumeClaim> items = KubernetesConfig.client.persistentVolumeClaims().inAnyNamespace().list().getItems();

        return items;
    }

    @Override
    public List<PersistentVolumeClaim> findPVCByNamespace(String namespace) {

        List<PersistentVolumeClaim> items = KubernetesConfig.client.persistentVolumeClaims().inNamespace(namespace).list().getItems();

        return items;
    }

    @Override
    public Boolean deletePVCByNameAndNamespace(String name, String namespace) {

        Boolean delete = KubernetesConfig.client.persistentVolumeClaims().inNamespace(namespace).withName(name).delete();

        return delete;
    }

    @Override
    public PersistentVolumeClaim loadPVCFromYaml(InputStream yamlInputStream) {

        PersistentVolumeClaim persistentVolumeClaim = KubernetesConfig.client.persistentVolumeClaims().load(yamlInputStream).get();

        return persistentVolumeClaim;
    }

    @Override
    public PersistentVolumeClaim createPVCByYaml(InputStream yamlInputStream) {

        PersistentVolumeClaim persistentVolumeClaim = KubernetesConfig.client.persistentVolumeClaims().load(yamlInputStream).get();
        String nameSpace = persistentVolumeClaim.getMetadata().getNamespace();
        persistentVolumeClaim = KubernetesConfig.client.persistentVolumeClaims().inNamespace(nameSpace).create(persistentVolumeClaim);

        return persistentVolumeClaim;
    }
}

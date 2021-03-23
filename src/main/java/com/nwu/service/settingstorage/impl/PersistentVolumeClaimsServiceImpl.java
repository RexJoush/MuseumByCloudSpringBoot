package com.nwu.service.settingstorage.impl;

import com.nwu.service.settingstorage.PersistentVolumeClaimsService;
import com.nwu.util.KubernetesConfig;
import io.fabric8.kubernetes.api.model.PersistentVolumeClaim;
import io.kubernetes.client.openapi.ApiException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Rex Joush
 * @time 2021.03.22
 */
@Service
public class PersistentVolumeClaimsServiceImpl implements PersistentVolumeClaimsService {

    public static void main(String[] args) throws ApiException {
        System.out.println(new PersistentVolumeClaimsServiceImpl().findPersistentVolumeClaimsByNamespace("default"));
    }


    @Override
    public List<PersistentVolumeClaim> findAllPersistentVolumeClaims() throws ApiException {

        List<PersistentVolumeClaim> itmes = KubernetesConfig.client.persistentVolumeClaims().inAnyNamespace().list().getItems();

        return itmes;
    }

    @Override
    public List<PersistentVolumeClaim> findPersistentVolumeClaimsByNamespace(String namespace) {
        return null;
    }
}

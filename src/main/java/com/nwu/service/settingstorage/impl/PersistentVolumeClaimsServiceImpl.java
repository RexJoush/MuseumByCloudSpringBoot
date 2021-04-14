package com.nwu.service.settingstorage.impl;

import com.nwu.service.settingstorage.PersistentVolumeClaimsService;
import com.nwu.util.KubernetesUtils;
import io.fabric8.kubernetes.api.model.PersistentVolumeClaim;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.models.V1PersistentVolumeClaim;
import io.kubernetes.client.util.Yaml;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import static com.nwu.util.GetYamlInputStream.byPath;

/**
 * @author Rex Joush
 * @time 2021.03.22
 */
@Service
public class PersistentVolumeClaimsServiceImpl implements PersistentVolumeClaimsService {


    @Override
    public List<PersistentVolumeClaim> findAllPVC() throws ApiException {

        List<PersistentVolumeClaim> items = KubernetesUtils.client.persistentVolumeClaims().inAnyNamespace().list().getItems();

        return items;
    }

    @Override
    public List<PersistentVolumeClaim> findPVCByNamespace(String namespace) {

        List<PersistentVolumeClaim> items = KubernetesUtils.client.persistentVolumeClaims().inNamespace(namespace).list().getItems();

        return items;
    }

    @Override
    public Boolean deletePVCByNameAndNamespace(String name, String namespace) {

        Boolean delete = KubernetesUtils.client.persistentVolumeClaims().inNamespace(namespace).withName(name).delete();

        return delete;
    }

    @Override
    public PersistentVolumeClaim loadPVCFromYaml(String path) throws FileNotFoundException {

        InputStream yamlInputStream = byPath(path);

        PersistentVolumeClaim persistentVolumeClaim = KubernetesUtils.client.persistentVolumeClaims().load(yamlInputStream).get();

        return persistentVolumeClaim;
    }

    @Override
    public PersistentVolumeClaim createPVCByYaml(String path) throws FileNotFoundException {

        InputStream yamlInputStream = byPath(path);

        PersistentVolumeClaim persistentVolumeClaim = KubernetesUtils.client.persistentVolumeClaims().load(yamlInputStream).get();
        String nameSpace = persistentVolumeClaim.getMetadata().getNamespace();
        try {
        persistentVolumeClaim = KubernetesUtils.client.persistentVolumeClaims().inNamespace(nameSpace).create(persistentVolumeClaim);
        }catch(Exception e){
            System.out.println("缺少必要的命名空间参数，或是已经有相同的资源对象，在PersistentVolumeClaimServiceImpl类的createPVCByYaml方法");
        }
        return persistentVolumeClaim;
    }

    @Override
    public PersistentVolumeClaim createOrReplacePVC(String path) throws FileNotFoundException {

        InputStream yamlInputStream = byPath(path);

        PersistentVolumeClaim pvc = KubernetesUtils.client.persistentVolumeClaims().load(yamlInputStream).get();
        String nameSpace = pvc.getMetadata().getNamespace();
        try {
        pvc = KubernetesUtils.client.persistentVolumeClaims().inNamespace(nameSpace).createOrReplace(pvc);
        }catch(Exception e){
            System.out.println("缺少必要的命名空间参数，或是已经有相同的资源对象，在PersistentVolumeClaimServiceImpl类的createOrReplacePVC方法");
        }
        return pvc;
    }

    @Override
    public PersistentVolumeClaim getPVCByNameAndNamespace(String name, String namespace) {
        return  KubernetesUtils.client.persistentVolumeClaims().inNamespace(namespace).withName(name).get();
    }

    @Override
    public String getPVCYamlByNameAndNamespace(String name, String namespace) throws ApiException {

        V1PersistentVolumeClaim v1PersistentVolumeClaim = KubernetesUtils.coreV1Api.readNamespacedPersistentVolumeClaim(name, namespace, null, null, null);
        
        return Yaml.dump(v1PersistentVolumeClaim);
    }
}

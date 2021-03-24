package com.nwu.service.settingstorage.impl;

import com.nwu.service.settingstorage.SecretsService;
import com.nwu.service.workload.impl.PodsServiceImpl;
import com.nwu.util.KubernetesConfig;
import io.fabric8.kubernetes.api.model.Secret;
import io.kubernetes.client.openapi.ApiException;
import org.checkerframework.checker.units.qual.K;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import static com.nwu.service.getYamlInputStream.byPath;

/**
 * @author Rex Joush
 * @time 2021.03.22
 */

/**
 * Secrets 的 service 层实现类
 */
@Service
public class SecretsServiceImpl implements SecretsService {

    public static void main(String[] args) throws ApiException {
        System.out.println(new SecretsServiceImpl().findSecretsByNamespace("default"));
    }


    @Override
    public List<Secret> findAllSecrets() throws ApiException {

        List<Secret> items = KubernetesConfig.client.secrets().inAnyNamespace().list().getItems();

        return items;
    }

    @Override
    public List<Secret> findSecretsByNamespace(String namespace) {

        List<Secret> items = KubernetesConfig.client.secrets().inNamespace(namespace).list().getItems();

        return items;
    }

    @Override
    public Boolean deleteSecretByNameAndNamespace(String name, String namespace) {

        Boolean delete = KubernetesConfig.client.secrets().inNamespace(namespace).withName(name).delete();

        return delete;
    }

    @Override
    public Secret loadSecretFromYaml(String path) throws FileNotFoundException {

        InputStream yamlInputStream = byPath(path);

        Secret secret = KubernetesConfig.client.secrets().load(yamlInputStream).get();

        return secret;
    }

    @Override
    public Secret createSecretByYaml(String path) throws FileNotFoundException {

        InputStream yamlInputStream = byPath(path);

        Secret secret = KubernetesConfig.client.secrets().load(yamlInputStream).get();
        String nameSpace = secret.getMetadata().getNamespace();
        try {
        secret = KubernetesConfig.client.secrets().inNamespace(nameSpace).create(secret);
        }catch(Exception e){
            System.out.println("缺少必要的命名空间参数，或是已经有相同的资源对象，在SecretServiceImpl类的createSecretByYaml方法");
        }
        return secret;
    }

    @Override
    public Secret createOrReplaceSecret(String path) throws FileNotFoundException {

        InputStream yamlInputStream = byPath(path);

        Secret secret = KubernetesConfig.client.secrets().load(yamlInputStream).get();
        String nameSpace = secret.getMetadata().getNamespace();

        try {
            secret = KubernetesConfig.client.secrets().inNamespace(nameSpace).createOrReplace(secret);
        }catch (Exception e){
            System.out.println("缺少必要的命名空间参数，或是已经有相同的资源对象，在SecretServiceImpl类的createOrReplaceSecret方法");
        }
        return secret;
    }


}

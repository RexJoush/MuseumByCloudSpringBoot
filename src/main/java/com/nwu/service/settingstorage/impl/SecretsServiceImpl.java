package com.nwu.service.settingstorage.impl;

import com.nwu.service.settingstorage.SecretsService;
import com.nwu.service.workload.impl.PodsServiceImpl;
import com.nwu.util.KubernetesConfig;
import io.fabric8.kubernetes.api.model.Secret;
import io.kubernetes.client.openapi.ApiException;
import org.springframework.stereotype.Service;

import java.util.List;

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
        System.out.println(new SecretsServiceImpl().findAllSecrets());
    }


    @Override
    public List<Secret> findAllSecrets() throws ApiException {

        List<Secret> items = KubernetesConfig.client.secrets().inAnyNamespace().list().getItems();

        return items;
    }

    @Override
    public List<Secret> findSecretsByNamespace(String namespace) {
        return null;
    }
}

package com.nwu.service.settingstorage.impl;

import com.nwu.service.settingstorage.ConfigMapsService;
import com.nwu.util.KubernetesConfig;
import io.fabric8.kubernetes.api.model.ConfigMap;
import io.kubernetes.client.openapi.ApiException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Rex Joush
 * @time 2021.03.22
 */

/**
 * ConfigMaps service层实现类
 */
@Service
public class ConfigMapsServiceImpl implements ConfigMapsService {


    public static void main(String[] args) throws ApiException {
        System.out.println(new ConfigMapsServiceImpl().findAllConfigMaps());
    }


    @Override
    public List<ConfigMap> findAllConfigMaps() throws ApiException {

        List<ConfigMap> items = KubernetesConfig.client.configMaps().inAnyNamespace().list().getItems();

        return items;

    }

    @Override
    public List<ConfigMap> findConfigMapsByNamespace(String namespace) {
        return null;
    }
}

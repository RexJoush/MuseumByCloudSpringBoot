package com.nwu.service.settingstorage.impl;

import com.nwu.service.settingstorage.ConfigMapsService;
import com.nwu.util.KubernetesConfig;
import io.fabric8.kubernetes.api.model.ConfigMap;
import io.kubernetes.client.openapi.ApiException;
import org.checkerframework.checker.units.qual.K;
import org.springframework.stereotype.Service;

import java.io.InputStream;
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


//    public static void main(String[] args) throws ApiException {
//        System.out.println(new ConfigMapsServiceImpl().loadConfigMapFromYaml( );
//    }


    @Override
    public List<ConfigMap> findAllConfigMaps() throws ApiException {

        List<ConfigMap> items = KubernetesConfig.client.configMaps().inAnyNamespace().list().getItems();

        return items;

    }

    @Override
    public List<ConfigMap> findConfigMapsByNamespace(String namespace) {

        List<ConfigMap> items = KubernetesConfig.client.configMaps().inNamespace(namespace).list().getItems();

        return items;
    }

    @Override
    public Boolean deleteConfigMapByNameAndNamespace(String name, String namespace) {

        Boolean delete = KubernetesConfig.client.secrets().inNamespace(namespace).withName(name).delete();

        return delete;
    }

    @Override
    public ConfigMap loadConfigMapFromYaml(InputStream yamlinputStream) {

        ConfigMap configMap = KubernetesConfig.client.configMaps().load(yamlinputStream).get();

        return configMap;
    }

    @Override
    public ConfigMap createConfigMapByYaml(InputStream yamlinputStream) {

        ConfigMap configMap = KubernetesConfig.client.configMaps().load(yamlinputStream).get();
        String nameSpace = configMap.getMetadata().getNamespace();
        try{
            configMap = KubernetesConfig.client.configMaps().inNamespace(nameSpace).create(configMap);
        }catch(Exception e){
            System.out.println("cuowu");
        }

        return configMap;
    }

    //@Override
//    public ConfigMap updateConfigMap(String name, String namespace) {
//
//        ConfigMap configMap = KubernetesConfig.client.configMaps().inNamespace(namespace).withName(name).edit()
//        //ConfigMap configMap1 = client.configMaps().inNamespace(currentNamespace).withName("configmap1").edit()
//          //      .addToData("4", "four").done();
//    }
}

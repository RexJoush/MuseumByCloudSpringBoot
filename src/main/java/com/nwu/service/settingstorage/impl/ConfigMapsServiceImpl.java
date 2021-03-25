package com.nwu.service.settingstorage.impl;

import com.nwu.service.settingstorage.ConfigMapsService;
import com.nwu.util.KubernetesUtils;
import io.fabric8.kubernetes.api.model.ConfigMap;
import io.kubernetes.client.openapi.ApiException;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import static com.nwu.util.GetYamlInputStream.byPath;

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

        List<ConfigMap> items = KubernetesUtils.client.configMaps().inAnyNamespace().list().getItems();

        return items;

    }

    @Override
    public List<ConfigMap> findConfigMapsByNamespace(String namespace)  {

        List<ConfigMap> items = KubernetesUtils.client.configMaps().inNamespace(namespace).list().getItems();

        return items;
    }

    @Override
    public Boolean deleteConfigMapByNameAndNamespace(String name, String namespace) {

        Boolean delete = KubernetesUtils.client.secrets().inNamespace(namespace).withName(name).delete();

        return delete;
    }

    @Override
    public ConfigMap loadConfigMapFromYaml(String path) throws FileNotFoundException {

        InputStream yamlInputStream = byPath(path);

        ConfigMap configMap = KubernetesUtils.client.configMaps().load(yamlInputStream).get();

        return configMap;
    }

    @Override
    public ConfigMap createConfigMapByYaml(String path) throws FileNotFoundException {

        InputStream yamlInputStream = byPath(path);

        ConfigMap configMap = KubernetesUtils.client.configMaps().load(yamlInputStream).get();
        String nameSpace = configMap.getMetadata().getNamespace();
        try{
            configMap = KubernetesUtils.client.configMaps().inNamespace(nameSpace).create(configMap);
        }catch(Exception e){
            System.out.println("缺少必要的命名空间参数，或是已经有相同的资源对象，在ConfigMapServiceImpl类的createConfigMapByYaml方法");
        }

        return configMap;
    }

    @Override
    public ConfigMap createOrReplaceConfigMap(String path) throws FileNotFoundException {

        InputStream yamlInputStream = byPath(path);

        ConfigMap configMap = KubernetesUtils.client.configMaps().load(yamlInputStream).get();
        String nameSpace = configMap.getMetadata().getNamespace();

        try {
            configMap = KubernetesUtils.client.configMaps().inNamespace(nameSpace).createOrReplace(configMap);
        }catch(Exception e){
            System.out.println("缺少必要的命名空间参数，或是已经有相同的资源对象，在ConfigMapServiceImpl类的createOrReplaceConfigMap方法");
        }
        return configMap;
    }

    //@Override
//    public ConfigMap updateConfigMap(String name, String namespace) {
//
//        ConfigMap configMap = KubernetesUtils.client.configMaps().inNamespace(namespace).withName(name).edit()
//        //ConfigMap configMap1 = client.configMaps().inNamespace(currentNamespace).withName("configmap1").edit()
//          //      .addToData("4", "four").done();
//    }
}

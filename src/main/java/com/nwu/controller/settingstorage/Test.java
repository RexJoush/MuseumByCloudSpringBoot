package com.nwu.controller.settingstorage;

import com.nwu.service.settingstorage.impl.ConfigMapsServiceImpl;
import io.fabric8.kubernetes.api.model.ConfigMap;
import io.kubernetes.client.openapi.ApiException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;


public class Test {
    public static void main(String[] args) throws FileNotFoundException, ApiException {
<<<<<<< HEAD
        String path = Test.class.getClassLoader().getResource("RS.yaml").getPath();
        File file = new File(path);
        FileInputStream fileInputStream = new FileInputStream(file);
        ConfigMapsServiceImpl configMapsServiceImpl = new ConfigMapsServiceImpl();


        ConfigMap configMapByYaml = configMapsServiceImpl.createConfigMapByYaml(fileInputStream);
        List<ConfigMap> allConfigMaps = configMapsServiceImpl.findAllConfigMaps();
        System.out.println(allConfigMaps);
        System.out.println("\n\n");
        String namespace = configMapByYaml.getMetadata().getNamespace();
        System.out.println(configMapByYaml);
        System.out.println(namespace);
=======
//        String path = Test.class.getClassLoader().getResource("configMap.yaml").getPath();
//        File file = new File(path);
//        FileInputStream fileInputStream = new FileInputStream(file);
//        ConfigMapsServiceImpl configMapsServiceImpl = new ConfigMapsServiceImpl();
//
//        ConfigMap configMapByYaml = configMapsServiceImpl.createConfigMapByYaml(fileInputStream);
//        List<ConfigMap> allConfigMaps = configMapsServiceImpl.findAllConfigMaps();
//        System.out.println(allConfigMaps);
//        System.out.println("\n\n");
//        String namespace = configMapByYaml.getMetadata().getNamespace();
//        System.out.println(configMapByYaml);
//        System.out.println(namespace);
>>>>>>> 680a31ede4ad7922feff4f2c40d61e099032257d

    }
}

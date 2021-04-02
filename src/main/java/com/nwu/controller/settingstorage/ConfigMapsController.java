package com.nwu.controller.settingstorage;

/**
 * @author Rex Joush
 * @time 2021.03.22
 */

import com.alibaba.fastjson.JSON;
import com.nwu.service.settingstorage.ConfigMapsService;
import com.nwu.service.settingstorage.impl.ConfigMapsServiceImpl;
import io.fabric8.kubernetes.api.model.ConfigMap;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.proto.V1;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Config Maps 的 controller 层
 */
@RestController
@RequestMapping("/configMaps")
public class ConfigMapsController {

    @Resource
    private ConfigMapsServiceImpl configMapsService;

    @RequestMapping("/getAllConfigMaps")
    public String findAllConfigMaps() throws ApiException{

        List<ConfigMap> configMaps = configMapsService.findAllConfigMaps();

        Map<String,Object> result = new HashMap<>();

        result.put("code",1200);
        result.put("message","获取ConfigMap列表成功");
        result.put("data",configMaps);

        return JSON.toJSONString(result);
    }

    @RequestMapping("/getConfigMapsByNamespace")
    public String findConfigMapsByNamespace(String namespace) throws ApiException{

        List<ConfigMap> v1ConfigMapList = configMapsService.findConfigMapsByNamespace(namespace);

        Map<String,Object> result = new HashMap<>();

        result.put("code",1200);
        result.put("message","获取 ConfigMap 列表成功");
        result.put("data",v1ConfigMapList);

        return JSON.toJSONString(result);
    }

    @RequestMapping("/deleteConfigMap")
    public String deleteConfigMapByNameAndNamespace(String name, String namespace) throws ApiException{

        Boolean isDeleteConfigMap = configMapsService.deleteConfigMapByNameAndNamespace(name, namespace);

        Map<String,Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "删除 ConfigMap 成功");
        result.put("data", isDeleteConfigMap);

        return JSON.toJSONString(result);
    }

    @RequestMapping("/loadConfigMap")
    public String loadConfigMapFromYaml(String path) throws FileNotFoundException {

        ConfigMap configMap = configMapsService.loadConfigMapFromYaml(path);

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "加载 ConfigMap 成功");
        result.put("data", configMap);

        return JSON.toJSONString(result);
    }

    @RequestMapping("/createConfigMap")
    public String createConfigMapByYaml(String path) throws FileNotFoundException {

        ConfigMap configMapByYaml = configMapsService.createConfigMapByYaml(path);

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "创建 ConfigMap 成功");
        result.put("data", configMapByYaml);

        return JSON.toJSONString(result);
    }

    @RequestMapping("/createOrReplaceConfigMap")
    public String createOrReplaceConfigMap(String path) throws FileNotFoundException {

        ConfigMap replaceConfigMap = configMapsService.createOrReplaceConfigMap(path);

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "创建或更新 ConfigMap 成功");
        result.put("data", replaceConfigMap);

        return JSON.toJSONString(result);
    }

    @RequestMapping("/getConfigMapByNameAndNamespace")
    public String getConfigMapByNameAndNamespace(String name, String namespace){

        ConfigMap configMap = configMapsService.getConfigMapByNameAndNamespace(name,namespace);

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "通过 name namespace 获取 ConfigMap 成功");
        result.put("data", configMap);

        return JSON.toJSONString(result);
    }

}

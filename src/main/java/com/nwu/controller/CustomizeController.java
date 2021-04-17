package com.nwu.controller;

/**
 * @author Rex Joush
 * @time 2021.03.22
 */

import com.alibaba.fastjson.JSON;
import com.nwu.service.CustomizeService;
import com.nwu.service.workload.impl.JobsServiceImpl;
import io.fabric8.kubernetes.api.model.apiextensions.v1.CustomResourceDefinition;
import io.fabric8.kubernetes.api.model.apiextensions.v1.CustomResourceDefinitionList;
import io.fabric8.kubernetes.api.model.batch.Job;
import io.kubernetes.client.openapi.ApiException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 自定义资源的 controller 层
 */
@RestController
@RequestMapping("/customize")
public class CustomizeController {

    @Resource
    private CustomizeService customizeService;

    @RequestMapping("/loadCustomResourceDefinition")
    public String loadCustomResourceDefinition(String path) throws ApiException, FileNotFoundException {


        CustomResourceDefinition customResourceDefinition = customizeService.loadCustomResourceDefinition(path);

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "加载 CRD 成功");
        result.put("data", customResourceDefinition);

        return JSON.toJSONString(result);

    }

    @RequestMapping("/createCustomResourceDefinition")
    public String createCustomResourceDefinition(String path) throws ApiException, FileNotFoundException {


        CustomResourceDefinition customResourceDefinition = customizeService.createCustomResourceDefinition(path);

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "加载 CRD 成功");
        result.put("data", customResourceDefinition);

        return JSON.toJSONString(result);

    }

    @RequestMapping("/getCustomResourceDefinition")
    public String getCustomResourceDefinition() throws ApiException, FileNotFoundException {


        List<CustomResourceDefinition> customResourceDefinitionList = customizeService.getCustomResourceDefinition();

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "加载 CRD 成功");
        result.put("data", customResourceDefinitionList);

        return JSON.toJSONString(result);
    }

    @RequestMapping("/deleteCustomResourceDefinition")
    public String deleteCustomResourceDefinition(String crdName) throws ApiException, FileNotFoundException {


        boolean deleted = customizeService.deleteCustomResourceDefinition(crdName);

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "加载boolean成功");
        result.put("data", deleted);

        return JSON.toJSONString(result);
    }

    @RequestMapping("/getCustomResourceDefinitionObject")
    public String getCustomResourceDefinitionObject(String nameSpace, String deviceName) throws ApiException, FileNotFoundException {


        Map<String, Object> deviceObject = customizeService.getCustomResourceDefinitionObject(nameSpace, deviceName);

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "加载 deviceObject 成功");
        result.put("data", deviceObject);

        return JSON.toJSONString(result);
    }

    @RequestMapping("/getCustomResourceDefinitionObjectList")
    public String getCustomResourceDefinitionObjectList(String namespace) throws ApiException, FileNotFoundException {


        Map<String, Object> deviceObject = customizeService.getCustomResourceDefinitionObjectList(namespace);

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "加载 deviceObject 成功");
        result.put("data", deviceObject);

        return JSON.toJSONString(result);
    }

    @RequestMapping("/getCustomResourceDefinitionByName")
    public String getCustomResourceDefinitionByName(String name) throws ApiException, FileNotFoundException {


        CustomResourceDefinition customResourceDefinition = customizeService.getCustomResourceDefinitionByName(name);

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "加载 CRD 成功");
        result.put("data", customResourceDefinition);

        return JSON.toJSONString(result);
    }

    @RequestMapping("/getCustomResourceDefinitionObjectListByName")
    public String getCustomResourceDefinitionObjectListByName(String crdName) throws ApiException, FileNotFoundException {

        Map<String, Object> objects = customizeService.getCustomResourceDefinitionObjectListByName(crdName);
        Map<String, Object> result = new HashMap<>();
        result.put("code", 1200);
        result.put("message", "加载 objects 成功");
        result.put("data", objects);

        return JSON.toJSONString(result);
    }

    @RequestMapping("/getCustomResourceDefinitionObjectByCrdNameAndObjNameAndNamespace")
    public String getCustomResourceDefinitionObjectByCrdNameAndObjNameAndNamespace(String crdName, String objName, String nameSpace) throws ApiException, FileNotFoundException {


        Map<String, Object> object = customizeService.getCustomResourceDefinitionObjectByCrdNameAndObjNameAndNamespace(crdName, objName, nameSpace);
        Map<String, Object> result = new HashMap<>();
        result.put("code", 1200);
        result.put("message", "加载 object 成功");
        result.put("data", object);

        return JSON.toJSONString(result);
    }
    @RequestMapping("/getCrdYamlByName")
    public String getCrdYamlByName(String crdName){
        String crdYaml = customizeService.getCrdYamlByName(crdName);

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "获取 CRD Yaml 成功");
        result.put("data", crdYaml);

        return JSON.toJSONString(result);
    }
}

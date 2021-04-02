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
    public String deleteCustomResourceDefinition(CustomResourceDefinition customResourceDefinition) throws ApiException, FileNotFoundException {


        boolean deleted= customizeService.deleteCustomResourceDefinition(customResourceDefinition);

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "加载boolean成功");
        result.put("data", deleted);

        return JSON.toJSONString(result);
    }
    @RequestMapping("/getCustomResourceDefinitionObject")
    public String getCustomResourceDefinitionObject(String nameSpace,String deviceName) throws ApiException, FileNotFoundException {


        Map<String,Object> deviceObject= customizeService.getCustomResourceDefinitionObject(nameSpace,deviceName);

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "加载boolean成功");
        result.put("data", deviceObject);

        return JSON.toJSONString(result);
    }
    @RequestMapping("/getCustomResourceDefinitionObjectList")
    public String getCustomResourceDefinitionObjectList(String nameSapce) throws ApiException, FileNotFoundException {


        Map<String,Object> deviceObject= customizeService.getCustomResourceDefinitionObjectList(nameSapce);

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "加载boolean成功");
        result.put("data", deviceObject);

        return JSON.toJSONString(result);
    }

}

package com.nwu.controller.explorebalancing;

/**
 * @author Rex Joush
 * @time 2021.03.22
 */

import com.alibaba.fastjson.JSON;
import com.nwu.entity.settingstorage.ServiceDefinition;
import com.nwu.service.explorebalancing.impl.ServicesServiceImpl;
import com.nwu.util.DealYamlStringFromFront;
import io.fabric8.kubernetes.api.model.Endpoints;
import io.fabric8.kubernetes.api.model.Service;
import io.kubernetes.client.openapi.ApiException;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Services 的 controller 层
 */
@RestController
@RequestMapping("/service")
public class ServicesController {

    @Resource
    private ServicesServiceImpl serviceService;


    @RequestMapping("/getAllServices")
    public String findAllServices(String namespace) throws ApiException {

        List<Service> services = serviceService.findAllServices();
        // 区分查询的命名空间
        if ("".equals(namespace)){
            services = serviceService.findAllServices();
        } else {
            services = serviceService.findServicesByNamespace(namespace);
        }
        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "获取 Service 列表成功");
        result.put("data",services);

        return JSON.toJSONString(result);

    }

    @RequestMapping("/getServicesByNamespace")
    public String findServicesByNamespace(String namespace) throws ApiException{

        List<Service> v1ServiceList = serviceService.findServicesByNamespace(namespace);

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "获取 Service 列表成功");
        result.put("data", v1ServiceList);

        return JSON.toJSONString(result);
    }


    @RequestMapping("/getServiceYamlByNameAndNamespace")
    public String getServiceYamlByNameAndNamespace(String name, String namespace) {

        String serviceYaml = serviceService.findServiceYamlByNameAndNamespace(name, namespace);

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "获取 Service Yaml 成功");
        result.put("data", serviceYaml);

        return JSON.toJSONString(result);
    }


    @RequestMapping("/getServiceByNameAndNamespace")
    public String getServiceByNameAndNamespace(String name, String namespace){

        ServiceDefinition service = serviceService.getServiceByNameAndNamespace(name,namespace);

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "通过 name namespace 获取 Service 成功");
        result.put("data", service);

        return JSON.toJSONString(result);
    }



    @RequestMapping("/loadServiceFromYaml")
    public String loadServiceFromYaml(String path) throws FileNotFoundException {

        Service service = serviceService.loadServiceFromYaml(path);

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "加载 Service 成功");
        result.put("data", service);

        return JSON.toJSONString(result);
    }


    @RequestMapping("/createServiceFromYaml")
    public String createServiceFromYaml(String path) throws FileNotFoundException {

        Service service = serviceService.createServiceByYaml(path);

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "创建 Service 成功");
        result.put("data", service);

        return JSON.toJSONString(result);
    }



    @RequestMapping("/delServiceByNameAndNamespace")
    public String deleteServiceByNameAndNamespace(String name, String namespace){

        Boolean deleteSvc = serviceService.deleteServiceByNameAndNamespace(name,namespace);

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "删除 Service 成功");
        result.put("data", deleteSvc);

        return JSON.toJSONString(result);


    }


    @RequestMapping("/createOrReplaceService")
    public String createOrReplaceService(String path) throws FileNotFoundException {

        Service service = serviceService.createOrReplaceService(path);

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "创建或更新 Service 成功");
        result.put("data", service);

        return JSON.toJSONString(result);
    }

    @RequestMapping("/getEndpointBySvcNameAndNamespace")
    public String getEndpointBySvcNameAndNamespace(String name, String namespace){

        Endpoints endpoint = serviceService.getEndpointBySvcNameAndNamespace(name,namespace);

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "通过 name namespace 获取 Endpoint 成功");
        result.put("data", endpoint);

        return JSON.toJSONString(result);
    }

    @RequestMapping("/changeServiceByYamlString")
    public String changeServiceByYamlString(@RequestBody String yaml) throws IOException {

        Map<String, Object> result = new HashMap<>();

        yaml = DealYamlStringFromFront.dealYamlStringFromFront(yaml);
        Pair<Integer, Boolean> pair = serviceService.createOrReplaceServiceByYamlString(yaml);

        result.put("code", pair.getLeft());
        if(pair.getLeft() == 1200) result.put("message", "创建成功");
        else result.put("message", "创建失败");
        result.put("data", pair.getRight());

        return JSON.toJSONString(result);
    }
}



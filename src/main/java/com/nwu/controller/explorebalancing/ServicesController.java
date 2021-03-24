package com.nwu.controller.explorebalancing;

/**
 * @author Rex Joush
 * @time 2021.03.22
 */

import com.alibaba.fastjson.JSON;
import com.nwu.service.explorebalancing.impl.ServicesServiceImpl;
import com.nwu.service.workload.impl.PodsServiceImpl;
import com.nwu.util.KubernetesConfig;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.Service;
import io.kubernetes.client.openapi.ApiException;
import org.checkerframework.checker.units.qual.K;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Services 的 controller 层
 */
@RestController
@RequestMapping("/services")
public class ServicesController {

    public static void main(String[] args) {
        System.out.println(new ServicesServiceImpl().findAllServices());
    }


    @Resource
    private ServicesServiceImpl serviceService;


    @RequestMapping("getAllServices")
    public String findAllServices() throws ApiException {

        List<Service> services = serviceService.findAllServices();

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "获取 Service 列表成功");
        result.put("data",services);

        return JSON.toJSONString(result);

    }

    @RequestMapping("getServicesByNamespace")
    public String findServicesByNamespace(String namespace) throws ApiException{

        List<Service> v1ServiceList = serviceService.findServicesByNamespace(namespace);

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "获取 Service 列表成功");
        result.put("data", v1ServiceList);

        return JSON.toJSONString(result);
    }



    @RequestMapping("loadServiceFromYaml")
    public String loadServiceFromYaml(String path) throws FileNotFoundException {

        Service service = serviceService.loadServiceFromYaml(path);

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "加载 Service 成功");
        result.put("data", service);

        return JSON.toJSONString(result);
    }


    @RequestMapping("createServiceFromYaml")
    public String createServiceFromYaml(String path) throws FileNotFoundException {

        Service service = serviceService.createServiceByYaml(path);

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "创建 Service 成功");
        result.put("data", service);

        return JSON.toJSONString(result);
    }



    @RequestMapping
    public String deleteServiceByNameAndNamespace(String name, String namespace){

        Boolean deleteSvc = serviceService.deleteServicesByNameAndNamespace(name,namespace);

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
}



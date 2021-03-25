package com.nwu.controller.explorebalancing;

/**
 * @author Rex Joush
 * @time 2021.03.22
 */

import com.alibaba.fastjson.JSON;
import com.nwu.service.explorebalancing.impl.IngressesServiceImpl;
import com.nwu.service.workload.impl.PodsServiceImpl;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.Service;
import io.fabric8.kubernetes.api.model.extensions.Ingress;
import io.kubernetes.client.openapi.ApiException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Ingresses 的 controller 层
 */
@RestController
@RequestMapping("/ingresses")
public class IngressesController {

    @Resource
    private IngressesServiceImpl ingressesService;

    @RequestMapping("/getAllIngresses")
    public String findAllIngresses() throws ApiException {

        List<Ingress> ingresses = ingressesService.findAllIngresses();

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "获取 Ingresses 列表成功");
        result.put("data", ingresses);

        return JSON.toJSONString(result);


    }

    @RequestMapping("/getServiceByNamespace")
    public String findServiceByNamespace(String namespace){

        List<Ingress> v1IngressesList = ingressesService.findIngressesByNamespace(namespace);

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "获取 Ingress 列表成功");
        result.put("data", v1IngressesList);

        return JSON.toJSONString(result);
    }

    @RequestMapping("/loadServiceFromYaml")
    public String loadServiceFromYaml(String path) throws FileNotFoundException {

        Ingress ingress = ingressesService.loadServiceFromYaml(path);

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "加载 ingress 成功");
        result.put("data", ingress);

        return JSON.toJSONString(result);
    }

    @RequestMapping("/createIngressesFromYaml")
    public String createIngressesFromYaml(String path) throws FileNotFoundException {

        Ingress ingress = ingressesService.createOrReplaceIngress(path);

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "创建 Ingress 成功");
        result.put("data", ingress);

        return JSON.toJSONString(result);
    }


    @RequestMapping("/deleteIngressByNameAndNamespace")
    public String deleteIngressByNameAndNamespace(String name, String namespace){

        Boolean deleteIngress = ingressesService.deleteIngressesByNameAndNamespace(name,namespace);

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "删除 Service 成功");
        result.put("data", deleteIngress);

        return JSON.toJSONString(result);

    }

    @RequestMapping("/createOrReplaceIngress")
    public String createOrReplaceIngress(String path) throws FileNotFoundException {

        Ingress ingress = ingressesService.createOrReplaceIngress(path);

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "创建或更新 Service 成功");
        result.put("data", ingress);

        return JSON.toJSONString(result);
    }
}
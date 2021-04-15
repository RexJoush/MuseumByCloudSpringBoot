package com.nwu.controller.cluster;


import com.alibaba.fastjson.JSON;
import com.nwu.entity.cluster.NamespaceDetails;
import com.nwu.entity.cluster.NamespaceName;
import com.nwu.service.cluster.impl.NamespacesServiceImpl;
import com.nwu.util.KubernetesUtils;
import io.fabric8.kubernetes.api.model.Namespace;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.models.V1Namespace;
import io.kubernetes.client.openapi.models.V1Service;
import io.kubernetes.client.util.Yaml;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zqy
 * @time 2020.03.24
 */

@RestController
@RequestMapping("/namespaces")
public class NamespacesController {

//    public static void main(String[] args) {
//        V1Namespace namespace = null;
//        try {
//            namespace = KubernetesUtils.coreV1Api.readNamespace("default", null, null, null);
//        } catch (ApiException e) {
//            e.printStackTrace();
//        }
//        System.out.println(Yaml.dump(namespace));
//    }

    @Resource
    private NamespacesServiceImpl namespacesService;

    @RequestMapping("/getAllNamespaces")
    public String getAllNamespaces() throws ApiException {

        List<Namespace> namespacesList = namespacesService.getAllNamespaces();

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "获取 Namespace 列表成功");
        result.put("data", namespacesList);

        return JSON.toJSONString(result);
    }
    @RequestMapping("/getAllNamespaceName")
    public String getAllNamespaceName(){

        List<NamespaceName> namespacesNameList = namespacesService.getAllNamespaceName();

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "获取 Namespace Name 列表成功");
        result.put("data", namespacesNameList);

        return JSON.toJSONString(result);
    }

    @RequestMapping("/getNamespaceDetails")
    public String getNamespaceDetails(String namespace){

        NamespaceDetails namespaceDetails = namespacesService.getNamespaceDetails(namespace);

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "获取 Namespace 详情成功");
        result.put("data", namespaceDetails);

        return JSON.toJSONString(result);
    }


    @RequestMapping("/deleteNamespaceByName")
    public String deleteNamespaceByName(String namespace){

        Boolean delete = namespacesService.deleteNamespaceByName(namespace);

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "删除 Namespace 成功");
        result.put("data", delete);

        return JSON.toJSONString(result);
    }

    @RequestMapping("/getNamespaceYamlByName")
    public String findNamespaceYamlByName(String namespace){

        String namespaceYamlByName = namespacesService.findNamespaceYamlByName(namespace);

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "通过 name 获取 Namespace 的 yaml 文件成功");
        result.put("data", namespaceYamlByName);

        return JSON.toJSONString(result);
    }

}

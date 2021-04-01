package com.nwu.controller.cluster;


import com.alibaba.fastjson.JSON;
import com.nwu.entity.cluster.NamespaceName;
import com.nwu.service.cluster.impl.NamespacesServiceImpl;
import io.fabric8.kubernetes.api.model.Namespace;
import io.kubernetes.client.openapi.ApiException;
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


    @RequestMapping("/deleteNamespaceByName")
    public String deleteNamespaceByName(String namespace){

        Boolean delete = namespacesService.deleteNamespaceByName(namespace);

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "删除 Namespace 成功");
        result.put("data", delete);

        return JSON.toJSONString(result);
    }

}

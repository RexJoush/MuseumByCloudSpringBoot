package com.nwu.controller.cluster;

import com.alibaba.fastjson.JSON;
import com.nwu.entity.cluster.Definition.ClusterRoleDefinition;
import com.nwu.service.cluster.impl.ClusterRolesServiceImpl;
import io.fabric8.kubernetes.api.model.Namespace;
import io.fabric8.kubernetes.api.model.rbac.ClusterRole;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author
 * @time 2020.03.24
 */

@RestController
@RequestMapping("/clusterRoles")
public class ClusterRolesController {

    @Resource
    private ClusterRolesServiceImpl clusterRolesService;

    @RequestMapping("/getAllClusterRoles")
    public String getAllClusterRoles(){

        List<ClusterRoleDefinition> clusterRoleList = clusterRolesService.getAllClusterRoles();

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "获取 ClusterRoleList 列表成功");
        result.put("data", clusterRoleList);

        return JSON.toJSONString(result);
    }

    @RequestMapping("/getClusterRoleDetails")
    public String getClusterRoleDetails(String clusterRole){

        ClusterRole clusterRoleDetails = clusterRolesService.getClusterRoleDetails(clusterRole);

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "获取 Cluster Role 详情成功");
        result.put("data", clusterRoleDetails);

        return JSON.toJSONString(result);
    }

    @RequestMapping("/getClusterRoleYamlByName")
    public String findClusterRoleYamlByName(String clusterRole){

        String clusterRoleYaml = clusterRolesService.findClusterRoleYamlByName(clusterRole);

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "获取 Cluster Role 详情成功");
        result.put("data", clusterRoleYaml);

        return JSON.toJSONString(result);
    }

    @RequestMapping("/delClusterRoleByName")
    public String delClusterRoleByName(String clusterRole){

        Boolean aBoolean = clusterRolesService.delClusterRoleByName(clusterRole);

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "获取 Cluster Role 详情成功");
        result.put("data", aBoolean);

        return JSON.toJSONString(result);
    }
}

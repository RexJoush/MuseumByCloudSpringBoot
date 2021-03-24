package com.nwu.controller.cluster;

import com.alibaba.fastjson.JSON;
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

        List<ClusterRole> clusterRoleList = clusterRolesService.getAllClusterRoles();

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "获取 ClusterRoleList 列表成功");
        result.put("data", clusterRoleList);

        return JSON.toJSONString(result);
    }
}
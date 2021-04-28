package com.nwu.service.cluster.impl;

import com.nwu.entity.cluster.Definition.ClusterRoleDefinition;
import com.nwu.service.cluster.ClusterRolesService;
import com.nwu.util.KubernetesUtils;
import io.fabric8.kubernetes.api.model.rbac.ClusterRole;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.models.V1ClusterRole;
import io.kubernetes.client.util.Yaml;
import org.checkerframework.checker.units.qual.C;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author  zqy
 * @time 2020.03.24
 */

@Service
public class ClusterRolesServiceImpl implements ClusterRolesService {

    @Override
    public List<ClusterRoleDefinition> getAllClusterRoles(){

        // 自定义 集群角色列表
        List<ClusterRoleDefinition> result = new ArrayList<>();

        // 获取集群角色列表
        List<ClusterRole> items = KubernetesUtils.client.rbac().clusterRoles().list().getItems();

        // 封装返回结果
        for (ClusterRole item : items) {
            ClusterRoleDefinition definition = new ClusterRoleDefinition();
            definition.setName(item.getMetadata().getName());
            definition.setTime(item.getMetadata().getCreationTimestamp().replaceAll("[TZ]", " "));
            result.add(definition);
        }

        return result;
    }

    @Override
    public ClusterRole getClusterRoleDetails(String clusterRoleName) {
        return KubernetesUtils.client.rbac().clusterRoles().withName(clusterRoleName).get();
    }

    @Override
    public String findClusterRoleYamlByName(String clusterRoleName){

        V1ClusterRole clusterRole = null;
        try {
            clusterRole = KubernetesUtils.rbacAuthorizationV1Api.readClusterRole(clusterRoleName,null);
        } catch (ApiException e) {
            e.printStackTrace();
        }
        return Yaml.dump(clusterRole);
    }

    @Override
    public Boolean delClusterRoleByName(String clusterRoleName) {

        return KubernetesUtils.client.rbac().clusterRoles().withName(clusterRoleName).delete();
    }
}

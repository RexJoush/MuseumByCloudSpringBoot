package com.nwu.service.cluster.impl;

import com.nwu.service.cluster.ClusterRolesService;
import com.nwu.util.KubernetesUtils;
import io.fabric8.kubernetes.api.model.rbac.ClusterRole;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.models.V1ClusterRole;
import io.kubernetes.client.util.Yaml;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author  zqy
 * @time 2020.03.24
 */

@Service
public class ClusterRolesServiceImpl implements ClusterRolesService {

    @Override
    public List<ClusterRole> getAllClusterRoles(){

        List<ClusterRole> items = KubernetesUtils.client.rbac().clusterRoles().list().getItems();

        return items;
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

        Boolean delete = KubernetesUtils.client.rbac().clusterRoles().withName(clusterRoleName).delete();

        return delete;
    }
}

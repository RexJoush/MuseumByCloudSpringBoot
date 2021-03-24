package com.nwu.service.cluster.impl;

import com.nwu.service.cluster.ClusterRolesService;
import com.nwu.util.KubernetesConfig;
import io.fabric8.kubernetes.api.model.rbac.ClusterRole;
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

        List<ClusterRole> items = KubernetesConfig.client.rbac().clusterRoles().list().getItems();

        return items;
    }
}

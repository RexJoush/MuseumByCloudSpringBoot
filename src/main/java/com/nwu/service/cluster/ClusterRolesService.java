package com.nwu.service.cluster;

import io.fabric8.kubernetes.api.model.rbac.ClusterRole;

import java.util.List;

/**
 * @author  zqy
 * @time 2020.03.24
 */

public interface ClusterRolesService {

    List<ClusterRole> getAllClusterRoles();

}

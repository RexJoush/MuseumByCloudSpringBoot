package com.nwu.service.cluster;

import io.fabric8.kubernetes.api.model.rbac.ClusterRole;

import java.util.List;

/**
 * @author  Rex Joush
 * @time 2020.03.24
 */

public interface ClusterRolesService {

    /**
     * 获取所有集群角色
     * @return 集群角色列表
     */
    List<ClusterRole> getAllClusterRoles();

    /**
     * 根据名称获取集群角色详情
     * @param clusterRoleName 集群角色名
     * @return 详情
     */
    ClusterRole getClusterRoleDetails(String clusterRoleName);

}

package com.nwu.service.cluster;

import com.nwu.entity.cluster.Definition.ClusterRoleDefinition;
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
    List<ClusterRoleDefinition> getAllClusterRoles();

    /**
     * 根据名称获取集群角色详情
     * @param clusterRoleName 集群角色名
     * @return 详情
     */
    ClusterRole getClusterRoleDetails(String clusterRoleName);


    /**
     * 根据名称获取集群角色的 yaml 文件
     * @param clusterRoleName 集群角色名
     * @return 详情
     */
    String findClusterRoleYamlByName(String clusterRoleName);

    /**
     * 根据名称删除集群角色
     * @param clusterRoleName 集群角色名
     * @return 详情
     */
    Boolean delClusterRoleByName(String clusterRoleName);

}

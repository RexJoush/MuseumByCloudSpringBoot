package com.nwu.service.cluster;

import com.nwu.entity.cluster.NamespaceName;
import io.fabric8.kubernetes.api.model.Namespace;

import java.util.List;

/**
 * @author zqy
 * @time 2020.03.24
 */

public interface NamespacesService {

    /**
     * 获取所有 Namespace
     * @return Namespace列表
     */
    List<Namespace> getAllNamespaces();

    /**
     * 获取所有 namespace 名称列表
     * @return namespace 名称列表
     */
    List<NamespaceName> getAllNamespaceName();

    /**
     * 按名称删除命名空间
     * @param namespace 名称
     * @return 删除结果 Bool
     */
    Boolean deleteNamespaceByName(String namespace);

}

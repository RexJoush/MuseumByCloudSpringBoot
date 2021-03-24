package com.nwu.service.cluster;

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
     * 按名称删除命名空间
     * @param namespace 名称
     * @return 删除结果 Bool
     */
    Boolean deleteNamespaceByName(String namespace);

}

package com.nwu.service.explorebalancing;

/**
 * @author Rex Joush
 * @time 2021.03.22
 */

import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.extensions.Ingress;
import io.kubernetes.client.openapi.ApiException;

import java.util.List;

/**
 * Ingresses 的 service 层接口
 */
public interface IngressesService {

    /**
     * 获取 Ingresses 列表
     * @return Ingresses 列表
     * @throws ApiException
     */
    List<Ingress> findAllIngresses() throws ApiException;
}

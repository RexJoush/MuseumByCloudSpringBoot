package com.nwu.service.workload;

import io.fabric8.kubernetes.api.model.Pod;
import io.kubernetes.client.openapi.ApiException;

import java.util.List;

/**
 * @author Rex Joush
 * @time 2021.03.22
 */

/**
 *  Pods 的 service 层接口
 */
public interface PodsService {

    /**
     * 获取 Pod 列表
     * @return pod 列表
     */
    List<Pod> findAllPods() throws ApiException;


    /**
     * 通过 namespace 获取 pod 列表
     * @param namespace namespace 名称
     * @return pod 列表
     */
    List<Pod> findPodsByNamespace(String namespace);
}

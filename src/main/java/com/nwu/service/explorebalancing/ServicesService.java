package com.nwu.service.explorebalancing;

/**
 * @author Rex Joush
 * @time 2021.03.22
 */

import com.google.protobuf.Api;
import com.nwu.util.KubernetesConfig;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.Service;
import io.kubernetes.client.openapi.ApiException;

import java.io.InputStream;
import java.util.List;

/**
 * Services 的 service 层接口
 */
public interface ServicesService {

    /**
     * 获取Service 列表
     * @return Services 列表
     * @throws ApiException
     */
    List<Service> findAllServices() throws ApiException;

    Service loadServiceFromYaml(InputStream yamlInputStream) throws ApiException;


    /**
     * 创建一个 Service
     * @return
     * @throws ApiException
     */
    Service createServiceYaml(InputStream yamlInputStream) throws ApiException;


    /**
     * 删除一个 Service
     * @param serviceName
     * @param namespace
     * @return
     * @throws ApiException
     */
    Boolean deleteServices(String serviceName,String namespace) throws ApiException;


}

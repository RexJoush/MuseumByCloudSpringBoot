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

    /**
     * 通过namespace获取service
     * @param namespace
     * @return service 列表
     * @throws ApiException
     */
    List<Service> findServicesByNamespace(String namespace) throws ApiException;

    /**
     * 通过yaml文件加载Service
     * @param yamlInputStream   yaml文件输入流 InputStream
     * @return 加载的Service
     * @throws ApiException
     */
    Service loadServiceFromYaml(InputStream yamlInputStream) throws ApiException;


    /**
     * 通过Yaml文件创建一个 Service
     * @return 创建的Service
     * @throws ApiException
     */
    Service createServiceByYaml(InputStream yamlInputStream) throws ApiException;


    /**
     * 删除一个 Service
     * @param serviceName
     * @param namespace
     * @return
     * @throws ApiException
     */
    Boolean deleteServicesByNameAndNamespace(String serviceName,String namespace) throws ApiException;


    /**
     * 通过yaml文件创建或者更新Service
     * @param yamlInputStream
     * @return 创建或者更新的Service
     * @throws ApiException
     */
    Service createOrReplaceService(InputStream yamlInputStream) throws ApiException;
}

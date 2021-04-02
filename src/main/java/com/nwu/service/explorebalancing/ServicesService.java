package com.nwu.service.explorebalancing;

/**
 * @author Rex Joush
 * @time 2021.03.22
 */

import io.fabric8.kubernetes.api.model.Service;
import io.kubernetes.client.openapi.ApiException;

import java.io.FileNotFoundException;
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
    List<Service> findAllServices();

    /**
     * 通过namespace获取service
     * @param namespace
     * @return service 列表
     * @throws ApiException
     */
    List<Service> findServicesByNamespace(String namespace);

    /**
     * 通过yaml文件加载Service
     * @param path   yaml文件输入流 InputStream
     * @return 加载的Service
     * @throws ApiException
     */
    Service loadServiceFromYaml(String path) throws FileNotFoundException;


    /**
     * 通过Yaml文件创建一个 Service
     * @return 创建的Service
     * @throws ApiException
     */
    Service createServiceByYaml(String path) throws FileNotFoundException;


    /**
     * 删除一个 Service
     * @param serviceName
     * @param namespace
     * @return
     * @throws ApiException
     */
    Boolean deleteServicesByNameAndNamespace(String serviceName,String namespace);


    /**
     * 通过yaml文件创建或者更新Service
     * @param path
     * @return 创建或者更新的Service
     * @throws ApiException
     */
    Service createOrReplaceService(String path) throws FileNotFoundException;


    /**
     * 通过名字和命名空间获取service
     * @param name 名字
     * @param namespace 命名空间
     * @return
     */
    Service getServiceByNameAndNamespace(String name, String namespace);
}

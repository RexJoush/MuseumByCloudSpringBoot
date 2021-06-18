package com.nwu.service.explorebalancing;

/**
 * @author Rex Joush
 * @time 2021.03.22
 */

import com.nwu.entity.settingstorage.ServiceDefinition;
import io.fabric8.kubernetes.api.model.Endpoints;
import io.fabric8.kubernetes.api.model.Service;
import io.kubernetes.client.openapi.ApiException;
import org.apache.commons.lang3.tuple.Pair;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

/**
 * Services 的 service 层接口
 */
public interface ServicesService {

    /**
     * 通过名字和命名空间查找 service 对应的yaml文件
     * @param name 名字
     * @param namespace 命名空间
     * @return
     */
    String findServiceYamlByNameAndNamespace(String name, String namespace);


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
     * @param name
     * @param namespace
     * @return
     * @throws ApiException
     */
    Boolean deleteServiceByNameAndNamespace(String name,String namespace);


    /**
     * 通过yaml文件创建或者更新Service
     * @param path
     * @return 创建或者更新的Service
     * @throws ApiException
     */
    Service createOrReplaceService(String path) throws FileNotFoundException;


    /**
     * 通过名字和命名空间获取 service 详情，包括 service 基本数据，pod 列表，endpoint 列表，event 列表
     * @param name 名字
     * @param namespace 命名空间
     * @return
     */
    ServiceDefinition getServiceByNameAndNamespace(String name, String namespace);


    /**
     * 通过Service名字和命名空间获取Endpoint
     * @param name Service名字
     * @param namespace Service命名空间
     * @return
     */
    Endpoints getEndpointBySvcNameAndNamespace(String name, String namespace);

    /**
     * 通过标签查找 Services
     * @param labels 标签
     * @return 找到的 Service 列表
     */
    List<Service> getServicesByLabels(Map<String, String> labels);

    /**
     * 创建或更新 Service
     * @param yaml 描述 Service 的 Yaml 格式字符串
     * @return 创建结果和执行代码
     */
    Pair<Integer, Boolean> createOrReplaceServiceByYamlString(String yaml);
}

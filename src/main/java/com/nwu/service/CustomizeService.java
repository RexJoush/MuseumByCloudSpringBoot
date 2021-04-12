package com.nwu.service;

/**
 * @author Rex Bernie
 * @time 2021.03.22
 */

import io.fabric8.kubernetes.api.model.apiextensions.v1.CustomResourceDefinition;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;


/**
 * 自定义资源的 service 层接口
 */
public interface CustomizeService {

    /**
     * 获取CustomResourceDefinition列表
     *
     * @return CustomResourceDefinition列表
     */
    List<CustomResourceDefinition> getCustomResourceDefinition();

    /**
     * 通过自定义资源的名字货期自定义资源
     *
     * @param name 自定义资源的名字
     * @return
     * @throws FileNotFoundException
     */
    CustomResourceDefinition getCustomResourceDefinitionByName(String name) throws FileNotFoundException;

    /**
     * 删除CustomResourceDefinition
     *
     * @param crdName 要删除的自定义资源名称
     * @return CustomResourceDefinition列表
     */
    boolean deleteCustomResourceDefinition(String crdName) throws FileNotFoundException;

    /**
     * 加载以一个CustomResourceDefinition
     *
     * @param path 加载自定义资源的yaml文件路径
     * @return CustomResourceDefinition列表
     */
    CustomResourceDefinition loadCustomResourceDefinition(String path) throws FileNotFoundException;

    /**
     * 创建以一个CustomResourceDefinition
     *
     * @param path 创建自定义资源的yaml文件路径
     * @return CustomResourceDefinition列表
     */
    CustomResourceDefinition createCustomResourceDefinition(String path) throws FileNotFoundException;

    /**
     * 对象的名字的和命名空间获取自定义资源的对象
     *
     * @param nameSpace  对象的命名空间
     * @param deviceName 设备的名字
     * @return
     */
    Map<String, Object> getCustomResourceDefinitionObject(String nameSpace, String deviceName);

    /**
     * 通过命名空间获取对象列表
     *
     * @param nameSpace 命名空间
     * @return
     */
    Map<String, Object> getCustomResourceDefinitionObjectList(String nameSpace);

    /**
     * 通过crd名字和命名空间和object名字获取对象
     *
     * @param crdName   自定义资源名
     * @param objName   对象名
     * @param nameSpace 对象所在的命名空间
     * @return
     * @throws FileNotFoundException
     */
    public Map<String, Object> getCustomResourceDefinitionObjectByCrdNameAndObjNameAndNamespace(String crdName, String objName, String nameSpace) throws FileNotFoundException;

    /**
     * 通过自定义资源的名字获取该自定义资源的对象列表
     *
     * @param crdName 自定义资源的名字
     * @return
     * @throws FileNotFoundException
     */
    public Map<String, Object> getCustomResourceDefinitionObjectListbyName(String crdName) throws FileNotFoundException;

    /**
     * 通过crdname获取crdYaml
     *
     * @param crdName
     * @return crdYaml
     */
    String getCrdYamlByName(String crdName);

}

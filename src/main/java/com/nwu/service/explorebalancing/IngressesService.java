package com.nwu.service.explorebalancing;

/**
 * @author Rex Joush
 * @time 2021.03.22
 */

import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.extensions.Ingress;
import io.kubernetes.client.openapi.ApiException;

import java.io.FileNotFoundException;
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


    /**
     * 通过namespace获取Ingress
     * @param namespace
     * @return
     */
    List<Ingress> findIngressesByNamespace(String namespace);


    /**
     * 通过yaml文件加载Ingress
     * @param path
     * @return
     */
    Ingress loadServiceFromYaml(String path) throws FileNotFoundException;


    /**
     * 通过yaml文件创建一个Ingress
     * @param path
     * @return
     */
    Ingress createIngressByYaml(String path) throws FileNotFoundException;

    /**
     * 通过名字和命名空间查找 ingress 对应的yaml文件
     * @param name 名字
     * @param namespace 命名空间
     * @return
     */
    String findIngressYamlByNameAndNamespace(String name, String namespace);


    /**
     * 删除一个Ingress
     * @param ingressName
     * @param namespace
     * @return
     */
    Boolean deleteIngressesByNameAndNamespace(String ingressName,String namespace);


    /**
     * 通过yaml文件创建或者更新Ingress
     * @param path
     * @return
     */
    Ingress createOrReplaceIngress(String path) throws FileNotFoundException;

    /**
     * 通过名字和命名空间获取ingress
     * @param name 名字
     * @param namespace 命名空间
     * @return
     */
    Ingress getIngressByNameAndNamespace(String name, String namespace);
}

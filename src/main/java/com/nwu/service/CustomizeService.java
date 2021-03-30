package com.nwu.service;

/**
 * @author Rex Joush
 * @time 2021.03.22
 */

import io.fabric8.kubernetes.api.model.apiextensions.v1.CustomResourceDefinition;
import io.fabric8.kubernetes.api.model.apiextensions.v1.CustomResourceDefinitionList;
import io.fabric8.kubernetes.api.model.batch.CronJob;

import java.io.FileNotFoundException;


/**
 * 自定义资源的 service 层接口
 */
public interface CustomizeService {

    /**
     * 获取CustomResourceDefinition列表

     * @return CustomResourceDefinition列表
     */
    CustomResourceDefinitionList getCustomResourceDefinition() ;

    /**
     * 删除CustomResourceDefinition
     * @param customResourceDefinition 要删除的自定义资源
     * @return CustomResourceDefinition列表
     */
    boolean  deleteCustomResourceDefinition(CustomResourceDefinition customResourceDefinition);

    /**
     * 加载以一个CustomResourceDefinition
     * @param path 加载自定义资源的yaml文件路径
     * @return CustomResourceDefinition列表
     */
    CustomResourceDefinition loadCustomResourceDefinition(String path)throws FileNotFoundException;

    /**
     * 创建以一个CustomResourceDefinition
     * @param path 加载自定义资源的yaml文件路径
     * @return CustomResourceDefinition列表
     */
    CustomResourceDefinition createCustomResourceDefinition(String path)throws FileNotFoundException;
}

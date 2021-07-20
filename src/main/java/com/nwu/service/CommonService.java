package com.nwu.service;

/**
 * @author Rex Joush
 * @time 2021.04.07
 */

import io.fabric8.kubernetes.api.model.Event;

import java.io.File;
import java.util.List;

/**
 * 公有方法 service 层接口
 * 修改资源，删除资源等等
 */
public interface CommonService {

    /**
     * 根据传入的 yaml 文件进行资源的修改
     * @param yaml yaml 文件字符串
     * @return 创建结果信息
     *          1200 表示成功
     *          1201 表示创建失败
     *          1202 表示文件有问题
     */
    int changeResourceByYaml(File yaml);

    public int changeDeploymentByYaml(File yaml);
    /**
     * 根据传入的 yaml 文件进行资源的修改
     * @param yaml yaml 文件字符串
     * @return 创建结果信息
     *          1200 表示成功
     *          1201 表示失败
     *          1202 表示文件有问题
     */
    int changeServicesByYaml(File yaml);

    /**
     * 根据传入的 yaml 文件进行资源的修改
     * @param yaml yaml 文件字符串
     * @return 创建结果信息
     *          1200 表示成功
     *          1201 表示失败
     *          1202 表示文件有问题
     */
    int changeIngressesByYaml(File yaml);

    int changeCrdByYaml(File yaml);
    int changeCrdObjectByYaml(File yaml,String crdName);

//    /**
//     * 获取指定资源的事件
//     * @param name 事件所关联对象名称
//     * @param namespace 事件所关联对象命名空间
//     * @param kind 事件所关联对象的类型
//     * @return 返回找到的 Event 列表
//     */
//    List<Event> getEventByInvolvedObjectNNK(String name, String namespace, String kind);
//
//    /**
//     * 获取指定资源的事件
//     * @param uid 事件所关联的对象的 UID
//     * @return 返回找到的 Event 列表
//     */
//    List<Event> getEventByInvolvedObjectUid(String uid);
}

package com.nwu.service;

/**
 * @author Rex Joush
 * @time 2021.04.07
 */

import java.io.File;

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

}

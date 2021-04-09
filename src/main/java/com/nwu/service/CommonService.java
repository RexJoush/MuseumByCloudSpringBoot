package com.nwu.service;

/**
 * @author Rex Joush
 * @time 2021.04.07
 */

/**
 * 公有方法 service 层接口
 * 修改资源，删除资源等等
 */
public interface CommonService {

    /**
     * 根据传入的 yaml 文件进行资源的修改
     * @param yaml yaml 文件字符串
     * @return 创建结果信息
     */
    String changeResourceByYaml(String yaml);

}

package com.nwu.service;



/**
 * @author Rex Joush
 * @time 2021.03.22
 */

/**
 * 负载预测的 service 层接口
 */
public interface LoadForecastingService {

    /**
     * 更新 pod 个数
     * @param name deployment 名称
     * @param namespace deployment
     * @param replicas 修改的数量
     * @return 修改结果
     */
    String expandShrinkPod(String name, String namespace, int replicas);

}

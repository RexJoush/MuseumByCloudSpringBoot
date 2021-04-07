package com.nwu.dao.workload;

import com.nwu.entity.workload.PodUsage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Rex Joush
 * @time 2021.03.29
 */

@Mapper
public interface PodUsageDao {

    /**
     * 获取某个 pod 的所有 cpu 利用率数据
     * @param podName 节点名称
     * @param namespace pod 所属命名空间
     * @return 利用率列表
     */
    List<PodUsage> findAll(String podName, String namespace);

    /**
     * 获取某个 pod 的最近一条数据
     * @param podName pod 名称
     * @param namespace pod 所属命名空间
     * @return 利用率值
     */
    PodUsage findLast(String podName, String namespace);

    /**
     * 获取某个 pod 最近二十分钟的数据
     * @param podName pod 名称
     * @param namespace pod 所属命名空间
     * @param time 二十分钟前的时间
     * @return 利用率列表
     */
    List<PodUsage> findRecentTwenty(String podName, String namespace, String time);

    /**
     * 获取某个节点最近一天的数据
     * @param podName 节点名称
     * @param namespace pod 所属命名空间
     * @param time 一天前的日期
     * @return 利用率列表
     */
    List<PodUsage> findRecentOneDay(String podName, String namespace, String time);

    /**
     * 删除某个 pod 两天前的数据
     * @param time 两天前的日期
     * @return 利用率列表
     */
    int delTwoDayAgo( String time);

    /**
     * 添加一条利用率记录
     * @param podUsage 添加的记录
     * @return 添加记录数
     */
    int addPodUsage (@Param("podUsage") PodUsage podUsage);

}

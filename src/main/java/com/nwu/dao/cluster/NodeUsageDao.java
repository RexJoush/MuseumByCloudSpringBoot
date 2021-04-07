package com.nwu.dao.cluster;

import com.nwu.entity.cluster.NodeUsage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Rex Joush
 * @time 2021.03.29
 */

@Mapper
public interface NodeUsageDao {

    /**
     * 获取某个节点的所有 cpu 利用率数据
     * @param nodeName 节点名称
     * @return 利用率列表
     */
    List<NodeUsage> findAll(String nodeName);

    /**
     * 获取某个节点最近一条数据
     * @param nodeName 节点名称
     * @return 利用率值
     */
    NodeUsage findLast(String nodeName);

    /**
     * 获取某个节点最近二十分钟的数据
     * @param nodeName 节点名称
     * @param time 二十分钟前的时间
     * @return 利用率列表
     */
    List<NodeUsage> findRecentTwenty(String nodeName, String time);

    /**
     * 获取某个节点最近一天的数据
     * @param nodeName 节点名称
     * @param time 一天前的日期
     * @return 利用率列表
     */
    List<NodeUsage> findRecentOneDay(String nodeName, String time);

    /**
     * 删除两天前的数据
     * @param time 两天前的时间
     * @return 删除的记录数
     */
    int delTwoDayAgo (String time);

    /**
     * 添加一条利用率记录
     * @param nodeUsage 添加的记录
     * @return 添加记录数
     */
    int addNodeUsage (@Param("nodeUsage") NodeUsage nodeUsage);

}

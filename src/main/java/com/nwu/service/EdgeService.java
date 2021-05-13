package com.nwu.service;

/**
 * @author Rex Joush
 * @time 2021.03.22
 */


import com.nwu.entity.cluster.graph.ClusterGraph;
import io.fabric8.kubernetes.api.model.Node;

import java.io.FileNotFoundException;
import java.util.List;

/**
 * 边缘节点的 service 层接口
 */
public interface EdgeService {

    /**
     * 获取所有 node 集合
     *
     * @return node 集合
     */
    List<Node> findAllEdgeNodes();

    /**
     * 通过 node 节点名称获取信息
     *
     * @param nodeName node 节点名
     * @return 当前节点信息
     */
    Node findEdgeNodeByName(String nodeName);

    /**
     * 获取边缘节点的拓扑图
     * @return 图表
     */
    ClusterGraph initEdgeGraph() throws FileNotFoundException;

    /**
     * 保存 node 节点的资源利用率信息
     * @throws InterruptedException
     */
    // void saveNodeUsage() throws InterruptedException;

    /**
     * 获取当前节点的近 20 分钟的利用率数据
     * @param nodeName 节点名称
     * @return 利用率列表
     */
    // List<NodeUsage> findRecentTwenty(String nodeName);
    String getLogs(String podName,String nameSpace);
}

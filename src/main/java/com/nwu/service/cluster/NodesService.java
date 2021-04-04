package com.nwu.service.cluster;

import com.nwu.entity.cluster.NodeUsage;
import io.fabric8.kubernetes.api.model.Node;

import java.util.List;
import java.util.Map;

/**
 * @author zqy
 * @time 2021.03.24
 */

public interface NodesService {

    /**
     * 获取所有 node 集合
     * @return node 集合
     */
    List<Map<String, Object>> findAllNodes();

    /**
     * 通过 node 节点名称获取信息
     * @param nodeName node 节点名
     * @return 当前节点信息
     */
    Node findNodeByName(String nodeName);

    /**
     * 保存 node 节点的资源利用率信息
     * @throws InterruptedException
     */
    void saveNodeUsage() throws InterruptedException;

    /**
     * 删除 node 节点两天前的资源利用率信息
     */
    void deleteNodeUsage();

    /**
     * 获取当前节点的近 20 分钟的利用率数据
     * @param nodeName 节点名称
     * @return 利用率列表
     */
    List<NodeUsage> findRecentTwenty(String nodeName);

    /**
     * 获取当前节点的近一天的利用率数据
     * @param nodeName 节点名称
     * @return 利用率列表
     */
    List<NodeUsage> findRecentOneDay(String nodeName);

}

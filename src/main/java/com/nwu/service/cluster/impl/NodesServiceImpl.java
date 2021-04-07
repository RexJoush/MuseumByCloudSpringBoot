package com.nwu.service.cluster.impl;

import com.nwu.dao.cluster.NodeUsageDao;
import com.nwu.entity.cluster.NodeUsage;
import com.nwu.service.cluster.NodesService;
import com.nwu.util.TimeUtils;
import io.fabric8.kubernetes.api.model.Node;
import com.nwu.util.KubernetesUtils;
import io.fabric8.kubernetes.api.model.metrics.v1beta1.NodeMetrics;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;


/**
 * @author zqy
 * @time 2020.03.24
 */

@Service
public class NodesServiceImpl implements NodesService {

    @Resource
    private NodeUsageDao nodeUsageDao;

    @Override
    public List<Map<String, Object>> findAllNodes(){

        List<Map<String, Object>> result = new ArrayList<>();


        /*
            所有的 node，master 节点的包含标签 ，node-type.，值为 normal-node
            所有的 edge 节点包含标签 withLabel("metadata.labels", "node-role.kubernetes.io/edge")
         */
        // 获取当前 node 节点信息
        List<Node> items = KubernetesUtils.client.nodes().withLabel("node-type","normal-node").list().getItems();

        for (Node item : items) {
            // 获取最近一条数据
            NodeUsage usage = nodeUsageDao.findLast(item.getMetadata().getName());
            Map<String, Object> map = new HashMap<>();
            // 放入节点信息
            map.put("node", item);
            // 放入利用率信息
            map.put("usage",usage);
            // 添加进结果集合
            result.add(map);
        }
        // 返回结果
        return result;
    }

    /**
     * 获取 node 的利用率情况，并存储
     * @throws InterruptedException
     */
    @Async
    @Override
    public void saveNodeUsage() throws InterruptedException {

        while (true) {
            List<NodeMetrics> items = KubernetesUtils.client.top().nodes().metrics().getItems();

            for (NodeMetrics item : items) {
                NodeUsage nodeUsage = new NodeUsage();
                // 设置节点名称
                nodeUsage.setNodeName(item.getMetadata().getName());
                // 设置 cpu 使用数
                nodeUsage.setCpu(item.getUsage().get("cpu").getAmount());
                // 设置内存使用数
                nodeUsage.setMemory(item.getUsage().get("memory").getAmount());

                // 设置时间
                nodeUsage.setTime(TimeUtils.sdf.format(new Date()));
                // System.out.println(nodeUsage);

                nodeUsageDao.addNodeUsage(nodeUsage);
            }

            // 每隔 60 秒保存一次
            Thread.sleep(1000*60);
        }
    }

    /**
     * 删除某节点两天前的利用率数据
     */
    @Async
    @Override
    public void deleteNodeUsage() {
        // 删除两天前的数据
        nodeUsageDao.delTwoDayAgo(TimeUtils.getTwoDayAgo());
    }

    /**
     * 获取近 20 分钟的 node 利用率信息
     * @param nodeName 节点名称
     * @return
     */
    @Override
    public List<NodeUsage> findRecentTwenty(String nodeName) {
        return nodeUsageDao.findRecentTwenty(nodeName, TimeUtils.getTwentyMinuteAgo());
    }

    /**
     * 获取近一天的 node 利用率信息
     * @param nodeName 节点名称
     * @return
     */
    @Override
    public List<NodeUsage> findRecentOneDay(String nodeName) {
        return nodeUsageDao.findRecentOneDay(nodeName, TimeUtils.getOneDayAgo());
    }

    public Node findNodeByName(String nodeName) {
        Node node = KubernetesUtils.client.nodes().withName(nodeName).get();
        return node;
    }
}

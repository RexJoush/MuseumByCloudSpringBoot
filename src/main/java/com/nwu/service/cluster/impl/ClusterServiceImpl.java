package com.nwu.service.cluster.impl;

/**
 * @author Rex Joush
 * @time 2021.04.13
 */

import com.nwu.entity.cluster.ClusterGraph;
import com.nwu.entity.cluster.GraphCategory;
import com.nwu.entity.cluster.GraphLink;
import com.nwu.entity.cluster.GraphNode;
import com.nwu.service.cluster.ClusterService;
import com.nwu.util.KubernetesUtils;
import io.fabric8.kubernetes.api.model.Node;

import java.util.ArrayList;
import java.util.List;

/**
 * Cluster Service 实现类
 */
public class ClusterServiceImpl implements ClusterService {

    /**
     * 初始化图
     * node 节点图大小为 66
     * pod 为 33
     * @return 图的数据
     */
    @Override
    public ClusterGraph initClusterGraph() {
        ClusterGraph graph = new ClusterGraph();
        List<GraphNode> nodes = new ArrayList<>();
        List<GraphLink> links = new ArrayList<>();
        List<GraphCategory> categories = new ArrayList<>();
        int index = 0; // id 值

        List<Node> items = KubernetesUtils.client.nodes().list().getItems();

        for (Node item : items) {
            GraphNode node = new GraphNode();
            node.setId(index++);
            node.setName(item.getMetadata().getName());
            node.setSymbolSize("66");
//            node.setX();
//            node.setY();
//            node.setValue();

        }


        return graph;
    }
}

package com.nwu.service.impl;


import com.nwu.service.EdgeService;
import com.nwu.util.KubernetesUtils;
import io.fabric8.kubernetes.api.model.Node;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Rex Joush
 * @time 2021.03.22
 */

/**
 * 边缘节点的 service 层实现类
 */
@Service
public class EdgeServiceImpl implements EdgeService {


    /*
        所有的 node，master 节点的包含标签 ，node-type.，值为 normal-node
        所有的 edge 节点包含标签 withLabel("metadata.labels", "node-role.kubernetes.io/edge")
     */
    @Override
    public List<Node> findAllEdgeNodes() {

        return KubernetesUtils.client.nodes().withLabel("node-role.kubernetes.io/edge", "").list().getItems();

    }

    @Override
    public Node findEdgeNodeByName(String nodeName) {

        return KubernetesUtils.client.nodes().withName(nodeName).get();

    }


}

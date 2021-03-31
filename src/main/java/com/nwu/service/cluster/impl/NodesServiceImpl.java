package com.nwu.service.cluster.impl;

import com.nwu.service.cluster.NodesService;
import io.fabric8.kubernetes.api.model.Node;
import com.nwu.util.KubernetesUtils;
import io.fabric8.kubernetes.api.model.metrics.v1beta1.NodeMetrics;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @auther zqy
 * @time 2020.03.24
 */

@Service
public class NodesServiceImpl implements NodesService {

    @Override
    // public List<Map<String, Object>> getAllNodes(){
    public List<Node> getAllNodes(){

        List<Map<String, Object>> i = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();

        // 获取当前 node 节点信息
        List<Node> items = KubernetesUtils.client.nodes().list().getItems();
        //map.put("node",items);

        // 获取 node 节点 top 信息
        //List<NodeMetrics> items1 = KubernetesUtils.client.top().nodes().metrics().getItems();
        //map.put("nodeTop", items1);
        //i.add(map);
        return items;
    }
}

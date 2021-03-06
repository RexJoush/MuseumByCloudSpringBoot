package com.nwu.controller;

/**
 * @author Rex Joush
 * @time 2021.03.22
 */

import com.alibaba.fastjson.JSON;
import com.nwu.entity.cluster.graph.ClusterGraph;
import com.nwu.service.impl.EdgeServiceImpl;
import io.fabric8.kubernetes.api.model.Node;
import io.kubernetes.client.openapi.ApiException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 边缘节点的 controller 层
 */
@RestController
@RequestMapping("/edge")
public class EdgeController {

    @Resource
    private EdgeServiceImpl nodesService;

    @RequestMapping("/getAllEdgeNodes")
    public String getAllNodes() throws ApiException {

        List<Node> list = nodesService.findAllEdgeNodes();

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "获取 Edge Node 列表成功");
        result.put("data", list);

        return JSON.toJSONString(result);
    }

    @RequestMapping("/getEdgeNodeByName")
    public String getEdgeNodeByName(String nodeName) throws ApiException {

        Node node = nodesService.findEdgeNodeByName(nodeName);

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "获取 Edge Node 列表成功");
        result.put("data", node);

        return JSON.toJSONString(result);
    }

    @RequestMapping("/getLogs")
    public String getLogs(String podName,String nameSpace) throws ApiException {

        String log=new EdgeServiceImpl().getLogs(podName,nameSpace);

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "获取 Edge Node 列表成功");
        result.put("data", log);

        return JSON.toJSONString(result);
    }
    @RequestMapping("/initEdgeGraph")
    public String initEdgeGraph() throws FileNotFoundException {
        Map<String, Object> result = new HashMap<>();

        ClusterGraph graph = nodesService.initEdgeGraph();

        result.put("code", 1200);
        result.put("message", "获取边缘图表成功");
        result.put("data", graph);

        return JSON.toJSONString(result);
    }


}

package com.nwu.controller.cluster;

import com.alibaba.fastjson.JSON;
import com.nwu.entity.cluster.NodeDefinition;
import com.nwu.entity.cluster.NodeUsage;
import com.nwu.service.cluster.impl.NodesServiceImpl;
import io.fabric8.kubernetes.api.model.Node;
import io.fabric8.kubernetes.api.model.batch.CronJob;
import io.kubernetes.client.openapi.ApiException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zqy
 * @time 2020.03.24
 */

@RestController
@RequestMapping("/nodes")
public class NodesController {

    @Resource
    private NodesServiceImpl nodesService;

    @RequestMapping("/getAllNodes")
    public String getAllNodes() throws ApiException {

        List<NodeDefinition> nodeList = nodesService.findAllNodes();

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "获取 Node 列表成功");
        result.put("data", nodeList);

        return JSON.toJSONString(result);
    }

    @RequestMapping("/getNodeByName")
    public String getNodeByName(String nodeName){
        Node node = nodesService.findNodeByName(nodeName);
        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "获取 node 信息成功");
        result.put("data", node);

        return JSON.toJSONString(result);
    }

    @RequestMapping("/getUsageRecentTwenty")
    public String getUsageRecentTwenty(String nodeName){
        List<NodeUsage> usages = nodesService.findRecentTwenty(nodeName);
        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "获取近 20 分钟利用率成功");
        result.put("data", usages);

        return JSON.toJSONString(result);
    }

    @RequestMapping("/getNodeYamlByName")
    public String getPodYamlByNameAndNamespace(String nodeName){
        String nodeYaml = nodesService.findPodYamlByNameAndNamespace(nodeName);

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "获取 Node Yaml 成功");
        result.put("data", nodeYaml);

        return JSON.toJSONString(result);
    }


}

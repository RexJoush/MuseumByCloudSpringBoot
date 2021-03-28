package com.nwu.controller.cluster;

import com.alibaba.fastjson.JSON;
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

        // List<Map<String, Object>> nodeList = nodesService.getAllNodes();
        List<Node> nodeList = nodesService.getAllNodes();

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "获取 Node 列表成功");
        result.put("data", nodeList);

        return JSON.toJSONString(result);
    }
}

package com.nwu.controller.cluster;

/**
 * @author Rex Joush
 * @time 2021.04.13
 */

import com.alibaba.fastjson.JSON;
import com.nwu.entity.cluster.graph.ClusterGraph;
import com.nwu.service.cluster.impl.ClusterServiceImpl;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 集群整体部分的 Controller 层
 */
@RestController
@RequestMapping("/cluster")
public class ClusterController {

    @Resource
    private ClusterServiceImpl clusterService;

    @RequestMapping("/initClusterGraph")
    public String initClusterGraph(){

        ClusterGraph graph = clusterService.initClusterGraph();

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "获取图表成功");
        result.put("data", graph);

        return JSON.toJSONString(result);
    }
}

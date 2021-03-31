package com.nwu.service.cluster;

import io.fabric8.kubernetes.api.model.Node;

import java.util.List;
import java.util.Map;

/**
 * @author zqy
 * @time 2021.03.24
 */

public interface NodesService {

    //List<Map<String, Object>> getAllNodes();
    List<Node> getAllNodes();

}

package com.nwu.service.cluster.impl;

import com.nwu.service.cluster.NodesService;
import io.fabric8.kubernetes.api.model.Node;
import com.nwu.util.KubernetesUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @auther zqy
 * @time 2020.03.24
 */

@Service
public class NodesServiceImpl implements NodesService {

    @Override
    public List<Node> getAllNodes(){

        List<Node> items = KubernetesUtils.client.nodes().list().getItems();

        return items;
    }
}

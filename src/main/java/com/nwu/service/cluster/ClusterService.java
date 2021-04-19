package com.nwu.service.cluster;

/**
 * @author Rex Joush
 * @time 2021.04.13
 */

import com.nwu.entity.cluster.graph.ClusterGraph;

/**
 * 集群整体部分的 service 层
 */
public interface ClusterService {

    /*
        初始化集群的拓扑图
     */
    ClusterGraph initClusterGraph();

}

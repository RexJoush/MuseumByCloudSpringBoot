package com.nwu.entity.cluster.graph;

/**
 * @author Rex Joush
 * @time 2021.04.13
 */

import java.util.List;

/**
 * 集群首页拓扑图的实体类
 */
public class ClusterGraph {

    List<GraphNode> nodes;
    List<GraphLink> links;
    List<GraphCategory> categories;

    public ClusterGraph() {
    }

    public ClusterGraph(List<GraphNode> nodes, List<GraphLink> links, List<GraphCategory> categories) {
        this.nodes = nodes;
        this.links = links;
        this.categories = categories;
    }

    public List<GraphNode> getNodes() {
        return nodes;
    }

    public void setNodes(List<GraphNode> nodes) {
        this.nodes = nodes;
    }

    public List<GraphLink> getLinks() {
        return links;
    }

    public void setLinks(List<GraphLink> links) {
        this.links = links;
    }

    public List<GraphCategory> getCategories() {
        return categories;
    }

    public void setCategories(List<GraphCategory> categories) {
        this.categories = categories;
    }

    @Override
    public String toString() {
        return "ClusterGraph{" +
                "nodes=" + nodes +
                ", links=" + links +
                ", categories=" + categories +
                '}';
    }
}



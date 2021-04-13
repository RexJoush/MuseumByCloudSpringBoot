package com.nwu.entity.cluster;

/**
 * @author Rex Joush
 * @time 2021.04.13
 */
// 拓扑图中的连接线
public class GraphLink  {

    private int source;
    private int target;

    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }

    public int getTarget() {
        return target;
    }

    public void setTarget(int target) {
        this.target = target;
    }

    public GraphLink(int source, int target) {
        this.source = source;
        this.target = target;
    }

    public GraphLink() {
    }
}

package com.nwu.entity.cluster;

/**
 * @author Rex Joush
 * @time 2021.04.13
 */
// 拓扑图中的图例
public class GraphCategory {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public GraphCategory(String name) {
        this.name = name;
    }

    public GraphCategory() {
    }

    @Override
    public String toString() {
        return "GraphCategory{" +
                "name='" + name + '\'' +
                '}';
    }
}

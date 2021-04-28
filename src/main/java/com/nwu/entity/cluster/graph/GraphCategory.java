package com.nwu.entity.cluster.graph;

/**
 * @author Rex Joush
 * @time 2021.04.13
 */
// 拓扑图中的图例
public class GraphCategory {

    private ItemStyle itemStyle;
    private String name;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ItemStyle getItemStyle() {
        return itemStyle;
    }

    public void setItemStyle(ItemStyle itemStyle) {
        this.itemStyle = itemStyle;
    }

    public GraphCategory(ItemStyle itemStyle, String name) {
        this.itemStyle = itemStyle;
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

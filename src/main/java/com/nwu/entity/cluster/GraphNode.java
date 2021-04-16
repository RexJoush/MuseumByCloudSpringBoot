package com.nwu.entity.cluster;

/**
 * @author Rex Joush
 * @time 2021.04.13
 */
// 拓扑图中的节点
public class GraphNode {
    private int id;
    private String name;
    private String symbolSize;
    private double x;
    private double y;
    private String value;
    private int category;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymbolSize() {
        return symbolSize;
    }

    public void setSymbolSize(String symbolSize) {
        this.symbolSize = symbolSize;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public GraphNode(int id, String name, String symbolSize, double x, double y, String value, int category) {
        this.id = id;
        this.name = name;
        this.symbolSize = symbolSize;
        this.x = x;
        this.y = y;
        this.value = value;
        this.category = category;
    }

    public GraphNode() {
    }

    @Override
    public String toString() {
        return "GraphNode{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", symbolSize='" + symbolSize + '\'' +
                ", x=" + x +
                ", y=" + y +
                ", value='" + value + '\'' +
                ", category=" + category +
                '}';
    }
}

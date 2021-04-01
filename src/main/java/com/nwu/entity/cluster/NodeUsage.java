package com.nwu.entity.cluster;

/**
 * @author Rex Joush
 * @time 2021.03.29
 */

/**
 * node 利用率的实体类
 */
public class NodeUsage {

    private int id;
    private String nodeName;    // 所属 node 名称
    private String cpu;         // cpu 资源
    private String memory;      // 内存资源
    private String time;        // 获取时间

    @Override
    public String toString() {
        return "NodeUsage{" +
                "id=" + id +
                ", nodeName='" + nodeName + '\'' +
                ", cpu=" + cpu +
                ", memory=" + memory +
                ", time='" + time + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getCpu() {
        return cpu;
    }

    public void setCpu(String cpu) {
        this.cpu = cpu;
    }

    public String getMemory() {
        return memory;
    }

    public void setMemory(String memory) {
        this.memory = memory;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public NodeUsage(String nodeName, String cpu, String memory, String time) {
        this.nodeName = nodeName;
        this.cpu = cpu;
        this.memory = memory;
        this.time = time;
    }

    public NodeUsage(int id, String nodeName, String cpu, String memory, String time) {
        this.id = id;
        this.nodeName = nodeName;
        this.cpu = cpu;
        this.memory = memory;
        this.time = time;
    }

    public NodeUsage() {
    }
}

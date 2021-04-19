package com.nwu.entity.cluster.Definition;

/**
 * @author Rex Joush
 * @time 2021.04.12
 */

/**
 * 集群信息的 node 实体类
 */
public class NodeDefinition {
    private String name;        // 名称
    private String status;      // 状态
    private double cpuUsage;    // cpu 利用率
    private double memoryUsage; // 内存利用率
    private String time;        // 创建时间
    private boolean schedule;   // 是否可调度

    @Override
    public String toString() {
        return "NodeDefinition{" +
                "name='" + name + '\'' +
                ", status='" + status + '\'' +
                ", cpuUsage='" + cpuUsage + '\'' +
                ", memoryUsage='" + memoryUsage + '\'' +
                ", time='" + time + '\'' +
                ", schedule=" + schedule +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getCpuUsage() {
        return cpuUsage;
    }

    public void setCpuUsage(double cpuUsage) {
        this.cpuUsage = cpuUsage;
    }

    public double getMemoryUsage() {
        return memoryUsage;
    }

    public void setMemoryUsage(double memoryUsage) {
        this.memoryUsage = memoryUsage;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isSchedule() {
        return schedule;
    }

    public void setSchedule(boolean schedule) {
        this.schedule = schedule;
    }

    public NodeDefinition(String name, String status, double cpuUsage, double memoryUsage, String time, boolean schedule) {
        this.name = name;
        this.status = status;
        this.cpuUsage = cpuUsage;
        this.memoryUsage = memoryUsage;
        this.time = time;
        this.schedule = schedule;
    }

    public NodeDefinition() {
    }
}

package com.nwu.entity.settingstorage;

public class ConfigMapDefinition {

    private String name;        // ConfigMap 名称
    private String namespace;   // ConfigMap 命名空间
    private String phase;       // ConfigMap 状态， Succeeded, Pending, Running
    private int restartCount;   // 重启次数
    private String cpuUsage;    // cpu 利用率
    private String memoryUsage; // 内存利用率
    private String nodeName;    // 所属节点
    private String podIP;       // 主机 ip 地址

    @Override
    public String toString() {
        return "ConfigMapDefinition{" +
                "name='" + name + '\'' +
                ", namespace='" + namespace + '\'' +
                ", phase='" + phase + '\'' +
                ", restartCount=" + restartCount +
                ", cpuUsage='" + cpuUsage + '\'' +
                ", memoryUsage='" + memoryUsage + '\'' +
                ", nodeName='" + nodeName + '\'' +
                ", podIP='" + podIP + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getPhase() {
        return phase;
    }

    public void setPhase(String phase) {
        this.phase = phase;
    }

    public int getRestartCount() {
        return restartCount;
    }

    public void setRestartCount(int restartCount) {
        this.restartCount = restartCount;
    }

    public String getCpuUsage() {
        return cpuUsage;
    }

    public void setCpuUsage(String cpuUsage) {
        this.cpuUsage = cpuUsage;
    }

    public String getMemoryUsage() {
        return memoryUsage;
    }

    public void setMemoryUsage(String memoryUsage) {
        this.memoryUsage = memoryUsage;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getPodIP() {
        return podIP;
    }

    public void setPodIP(String podIP) {
        this.podIP = podIP;
    }

    public ConfigMapDefinition(String name, String namespace, String phase, int restartCount, String cpuUsage, String memoryUsage, String nodeName, String podIP) {
        this.name = name;
        this.namespace = namespace;
        this.phase = phase;
        this.restartCount = restartCount;
        this.cpuUsage = cpuUsage;
        this.memoryUsage = memoryUsage;
        this.nodeName = nodeName;
        this.podIP = podIP;
    }
}

package com.nwu.entity.workload;

/**
 * @author Rex Joush
 * @time 2021.03.31
 */
/**
 * pod 利用率的实体类
 */
public class PodUsage {

    private int id;
    private String podName;    // 所属 pod 名称
    private String namespace;  // pod 所属命名空间
    private String cpu;        // cpu 资源
    private String memory;     // 内存资源
    private String time;       // 获取时间

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPodName() {
        return podName;
    }

    public void setPodName(String podName) {
        this.podName = podName;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
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

    public PodUsage(String podName, String namespace, String cpu, String memory, String time) {
        this.podName = podName;
        this.namespace = namespace;
        this.cpu = cpu;
        this.memory = memory;
        this.time = time;
    }

    public PodUsage(int id, String podName, String namespace, String cpu, String memory, String time) {
        this.id = id;
        this.podName = podName;
        this.namespace = namespace;
        this.cpu = cpu;
        this.memory = memory;
        this.time = time;
    }

    public PodUsage() {
    }
}

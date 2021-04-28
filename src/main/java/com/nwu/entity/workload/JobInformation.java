package com.nwu.entity.workload;

/**
 * @author zqy
 * @time 2021.04.16
 */

/**
 * Job 类
 * 保存前端需要信息
 */
public class JobInformation {
    private String status;//状态
    private String name;//名称
    private String namespace;//命名空间
    private int runningPods;//正在运行的Pods数量
    private int replicas;//期望的Pods数量
    private String creationTimestamp;//创建时间

    public JobInformation() {
    }

    public JobInformation(String status, String name, String namespace, int runningPods, int replicas, String creationTimestamp) {
        this.status = status;
        this.name = name;
        this.namespace = namespace;
        this.runningPods = runningPods;
        this.replicas = replicas;
        this.creationTimestamp = creationTimestamp;
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

    public int getRunningPods() {
        return runningPods;
    }

    public void setRunningPods(int runningPods) {
        this.runningPods = runningPods;
    }

    public int getReplicas() {
        return replicas;
    }

    public void setReplicas(int replicas) {
        this.replicas = replicas;
    }

    public String getCreationTimestamp() {
        return creationTimestamp;
    }

    public void setCreationTimestamp(String creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "JobInformation{" +
                "status='" + status + '\'' +
                ", name='" + name + '\'' +
                ", namespace='" + namespace + '\'' +
                ", runningPods=" + runningPods +
                ", replicas=" + replicas +
                ", creationTimestamp='" + creationTimestamp + '\'' +
                '}';
    }
}

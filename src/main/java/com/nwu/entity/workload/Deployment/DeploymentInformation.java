package com.nwu.entity.workload.Deployment;

import java.util.Map;

/**
 * @author zqy
 * @time 2021.04.19
 */
public class DeploymentInformation {
    // 基本信息
    private String status;// 状态
    private String name;// 名称
    private String namespace;// 命名空间
    private int runningPods;// 正在运行的Pods数量
    private int replicas;// 期望的Pods数量
    private String creationTimestamp;// 创建时间

    // 详细信息
    private String uid; // uid 标识
    private Map<String, String> labels;// 标签
    private Map<String, String> annotation;// 描述信息
    private String policy;// Deployment 更新策略
    private String minReadySeconds;// 最小准备秒数 Pod Ready后度过这个秒数认为Pod可用
    private String revisionHistoryLimit;// 调整历史记录限制 RS保存数量
    private  Map<String, String> matchLabels;// 标签选择

    // 附加信息
    private String alias; // 别名

    public DeploymentInformation() {
    }

    public DeploymentInformation(String status, String name, String namespace, int runningPods, int replicas, String creationTimestamp, String uid, Map<String, String> labels, Map<String, String> annotation, String policy, String minReadySeconds, String revisionHistoryLimit, Map<String, String> matchLabels, String alias) {
        this.status = status;
        this.name = name;
        this.namespace = namespace;
        this.runningPods = runningPods;
        this.replicas = replicas;
        this.creationTimestamp = creationTimestamp;
        this.uid = uid;
        this.labels = labels;
        this.annotation = annotation;
        this.policy = policy;
        this.minReadySeconds = minReadySeconds;
        this.revisionHistoryLimit = revisionHistoryLimit;
        this.matchLabels = matchLabels;
        this.alias = alias;
    }

    @Override
    public String toString() {
        return "DeploymentInformation{" +
                "status='" + status + '\'' +
                ", name='" + name + '\'' +
                ", namespace='" + namespace + '\'' +
                ", runningPods=" + runningPods +
                ", replicas=" + replicas +
                ", creationTimestamp='" + creationTimestamp + '\'' +
                ", uid='" + uid + '\'' +
                ", labels=" + labels +
                ", annotation=" + annotation +
                ", policy='" + policy + '\'' +
                ", minReadySeconds='" + minReadySeconds + '\'' +
                ", revisionHistoryLimit='" + revisionHistoryLimit + '\'' +
                ", matchLabels=" + matchLabels +
                '}';
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Map<String, String> getLabels() {
        return labels;
    }

    public void setLabels(Map<String, String> labels) {
        this.labels = labels;
    }

    public Map<String, String> getAnnotation() {
        return annotation;
    }

    public void setAnnotation(Map<String, String> annotation) {
        this.annotation = annotation;
    }

    public String getPolicy() {
        return policy;
    }

    public void setPolicy(String policy) {
        this.policy = policy;
    }

    public String getMinReadySeconds() {
        return minReadySeconds;
    }

    public void setMinReadySeconds(String minReadySeconds) {
        this.minReadySeconds = minReadySeconds;
    }

    public String getRevisionHistoryLimit() {
        return revisionHistoryLimit;
    }

    public void setRevisionHistoryLimit(String revisionHistoryLimit) {
        this.revisionHistoryLimit = revisionHistoryLimit;
    }

    public Map<String, String> getMatchLabels() {
        return matchLabels;
    }

    public void setMatchLabels(Map<String, String> matchLabels) {
        this.matchLabels = matchLabels;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }
}

package com.nwu.entity.workload;

import java.util.Map;

/**
 * @author zqy
 * @time 2021.04.19
 */
public class CronJobInformation {
    //基础信息
    private String status;//状态
    private String name;//名称
    private String namespace;//命名空间
    private String schedule;//调度
    private int runningJobs;//正在运行的Jobs数量
    private String lastSchedulingTime;//最后调度时间
    private String creationTimestamp;//创建时间

    //详细信息
    private String uid;
    private Map<String, String> labels;
    private Map<String, String> annotation;
    private String suSpend;
    private String concurrencyPolicy;

    public CronJobInformation() {
    }

    public CronJobInformation(String status, String name, String namespace, String schedule, int runningJobs, String lastSchedulingTime, String creationTimestamp, String uid, Map<String, String> labels, Map<String, String> annotation, String suSpend, String concurrencyPolicy) {
        this.status = status;
        this.name = name;
        this.namespace = namespace;
        this.schedule = schedule;
        this.runningJobs = runningJobs;
        this.lastSchedulingTime = lastSchedulingTime;
        this.creationTimestamp = creationTimestamp;
        this.uid = uid;
        this.labels = labels;
        this.annotation = annotation;
        this.suSpend = suSpend;
        this.concurrencyPolicy = concurrencyPolicy;
    }

    public String toString(Boolean detailed) {
        if(detailed){
            return "CronJobInformation{" +
                    "status='" + status + '\'' +
                    ", name='" + name + '\'' +
                    ", namespace='" + namespace + '\'' +
                    ", schedule='" + schedule + '\'' +
                    ", runningJobs=" + runningJobs +
                    ", lastSchedulingTime='" + lastSchedulingTime + '\'' +
                    ", creationTimestamp='" + creationTimestamp + '\'' +
                    ", uid='" + uid + '\'' +
                    ", labels=" + labels +
                    ", annotation=" + annotation +
                    ", suSpend='" + suSpend + '\'' +
                    ", concurrencyPolicy='" + concurrencyPolicy + '\'' +
                    '}';
        }
        return "CronJobInformation{" +
                "status='" + status + '\'' +
                ", name='" + name + '\'' +
                ", namespace='" + namespace + '\'' +
                ", schedule='" + schedule + '\'' +
                ", runningJobs=" + runningJobs +
                ", lastSchedulingTime='" + lastSchedulingTime + '\'' +
                ", creationTimestamp='" + creationTimestamp + '\'' +
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

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public int getRunningJobs() {
        return runningJobs;
    }

    public void setRunningJobs(int runningJobs) {
        this.runningJobs = runningJobs;
    }

    public String getLastSchedulingTime() {
        return lastSchedulingTime;
    }

    public void setLastSchedulingTime(String lastSchedulingTime) {
        this.lastSchedulingTime = lastSchedulingTime;
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

    public String getSuSpend() {
        return suSpend;
    }

    public void setSuSpend(String suSpend) {
        this.suSpend = suSpend;
    }

    public String getConcurrencyPolicy() {
        return concurrencyPolicy;
    }

    public void setConcurrencyPolicy(String concurrencyPolicy) {
        this.concurrencyPolicy = concurrencyPolicy;
    }
}

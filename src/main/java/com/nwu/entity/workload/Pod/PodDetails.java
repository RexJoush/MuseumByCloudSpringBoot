package com.nwu.entity.workload.Pod;

/**
 * @author Rex Joush
 * @time 2021.04.05
 */

import io.fabric8.kubernetes.api.model.Pod;

import java.util.List;

/**
 * pod 详情页面实体类
 */
public class PodDetails {
    private Pod pod;    // pod 信息
    List<PodUsage> podUsagesList; // pod 利用率信息

    @Override
    public String toString() {
        return "PodDetails{" +
                "pod=" + pod +
                ", podUsagesList=" + podUsagesList +
                '}';
    }

    public Pod getPod() {
        return pod;
    }

    public void setPod(Pod pod) {
        this.pod = pod;
    }

    public List<PodUsage> getPodUsagesList() {
        return podUsagesList;
    }

    public void setPodUsagesList(List<PodUsage> podUsagesList) {
        this.podUsagesList = podUsagesList;
    }

    public PodDetails() {
    }

    public PodDetails(Pod pod, List<PodUsage> podUsagesList) {
        this.pod = pod;
        this.podUsagesList = podUsagesList;
    }
}

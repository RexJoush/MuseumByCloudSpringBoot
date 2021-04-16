package com.nwu.entity.workload;

/**
 * @author zqy
 * @time 2021.04.16
 */

/**
 * Job 类
 * 保存前端需要信息
 */
public class zqyJob {
    private String status;
    private String name;
    private String namespace;
    private int isOkPods;
    private int replicas;
    private String creationTimestamp;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

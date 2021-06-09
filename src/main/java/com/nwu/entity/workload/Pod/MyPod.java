package com.nwu.entity.workload.Pod;

import io.fabric8.kubernetes.api.model.*;

/**
 * @author Rex Joush
 * @time 2021.04.08
 */

public class MyPod implements HasMetadata, Namespaced {


    private String apiVersion = "v1";

    private String kind = "Pod";
    private ObjectMeta metadata;
    private PodSpec spec;
    private PodStatus status;

    @Override
    public String getApiVersion() {
        return apiVersion;
    }

    @Override
    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public PodSpec getSpec() {
        return spec;
    }

    public void setSpec(PodSpec spec) {
        this.spec = spec;
    }

    public PodStatus getStatus() {
        return status;
    }

    public void setStatus(PodStatus status) {
        this.status = status;
    }

    @Override
    public ObjectMeta getMetadata() {
        return null;
    }

    @Override
    public void setMetadata(ObjectMeta objectMeta) {

    }

    @Override
    public void setApiVersion(String s) {

    }
}

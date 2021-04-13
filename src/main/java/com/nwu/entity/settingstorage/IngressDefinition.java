package com.nwu.entity.settingstorage;

import io.fabric8.kubernetes.api.model.extensions.Ingress;

/**
 * ingress 详情的实体类
 */
public class IngressDefinition {
    private Ingress ingress;    //ingress 详情

    @Override
    public String toString() {
        return "IngressDefinition{" +
                "ingress=" + ingress +
                '}';
    }

    public Ingress getIngress() {
        return ingress;
    }

    public void setIngress(Ingress ingress) {
        this.ingress = ingress;
    }

    public IngressDefinition(Ingress ingress) {
        this.ingress = ingress;
    }

    public IngressDefinition() {

    }
}

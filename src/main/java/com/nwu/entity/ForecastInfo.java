package com.nwu.entity;

import java.util.Date;

public class ForecastInfo {
    private  Date datatime;
    private String deployment;
    private String namespace;
    ForecastInfo(){

    }
    public Date getDatatime() {
        return datatime;
    }

    public void setDatatime(Date datatime) {
        this.datatime = datatime;
    }

    public String getDeployment() {
        return deployment;
    }

    public void setDeployment(String deployment) {
        this.deployment = deployment;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }
}

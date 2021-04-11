package com.nwu.entity.settingstorage;

import com.nwu.entity.workload.PodDefinition;
import io.fabric8.kubernetes.api.model.Endpoints;
import io.fabric8.kubernetes.api.model.Event;
import io.fabric8.kubernetes.api.model.Service;

import java.util.List;

/**
 * service 详情的实体类
 */
public class ServiceDefinition {

    private Service service;            // service 详情
    private Endpoints endpoints;        // service 下的 endpoint 列表
    private List<PodDefinition> pods;   // service 下的 pod 列表
    private Event event;                // service 下的 event

    @Override
    public String toString() {
        return "ServiceDefinition{" +
                "service=" + service +
                ", endpoints=" + endpoints +
                ", pods=" + pods +
                ", event=" + event +
                '}';
    }

    public Endpoints getEndpoints() {
        return endpoints;
    }

    public void setEndpoints(Endpoints endpoints) {
        this.endpoints = endpoints;
    }

    public List<PodDefinition> getPods() {
        return pods;
    }

    public void setPods(List<PodDefinition> pods) {
        this.pods = pods;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public ServiceDefinition(Service service, Endpoints endpoints, List<PodDefinition> pods, Event event) {
        this.service = service;
        this.endpoints = endpoints;
        this.pods = pods;
        this.event = event;
    }

    public ServiceDefinition() {
    }
}

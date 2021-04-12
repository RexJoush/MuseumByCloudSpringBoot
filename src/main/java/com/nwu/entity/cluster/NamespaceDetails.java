package com.nwu.entity.cluster;

/**
 * @author Rex Joush
 * @time 2021.04.12
 */

import com.nwu.entity.common.EventDefinition;
import io.fabric8.kubernetes.api.model.Namespace;

import java.util.List;

/**
 * 命名空间详情的实体类
 */
public class NamespaceDetails {

    private Namespace namespace;            // 命名空间信息
    private List<EventDefinition> events;   // 事件列表

    @Override
    public String toString() {
        return "NamespaceDetails{" +
                "namespace=" + namespace +
                ", events=" + events +
                '}';
    }

    public Namespace getNamespace() {
        return namespace;
    }

    public void setNamespace(Namespace namespace) {
        this.namespace = namespace;
    }

    public List<EventDefinition> getEvents() {
        return events;
    }

    public void setEvents(List<EventDefinition> events) {
        this.events = events;
    }

    public NamespaceDetails(Namespace namespace, List<EventDefinition> events) {
        this.namespace = namespace;
        this.events = events;
    }

    public NamespaceDetails() {
    }
}

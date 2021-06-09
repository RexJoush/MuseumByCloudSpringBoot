package com.nwu.entity.customize;

import io.fabric8.kubernetes.api.model.Event;

import java.util.Map;

public class ObjectDefinition {

    private Map<String, Object> objectdefinition;            // service 详情
    private Event  event;                // service 下的 event

    @Override
    public String toString() {
        return "ObjectDefinition{" +
                "objectdefinition=" + objectdefinition +
                ", event=" + event +
                '}';
    }

    public Map<String, Object> getObjectdefinition() {
        return objectdefinition;
    }

    public void setObjectdefinition(Map<String, Object> objectdefinition) {
        this.objectdefinition = objectdefinition;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }
    public ObjectDefinition(){
    }
    public ObjectDefinition(Map<String, Object> objectdefinition,Event event){
        this.objectdefinition=objectdefinition;
        this.event=event;

    }
}

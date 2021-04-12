package com.nwu.entity.common;

/**
 * @author Rex Joush
 * @time 2021.04.12
 */

public class EventDefinition {

    private String message;     // 信息
    private String resource;    // 资源
    private String childObject; // 子对象
    private int count;          // 次数
    private String firstTime;   // 第一次的时间
    private String lastTime;    // 最后一次的时间

    @Override
    public String toString() {
        return "EventDefinition{" +
                "message='" + message + '\'' +
                ", resource='" + resource + '\'' +
                ", childObject='" + childObject + '\'' +
                ", count=" + count +
                ", firstTime='" + firstTime + '\'' +
                ", lastTime='" + lastTime + '\'' +
                '}';
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public String getChildObject() {
        return childObject;
    }

    public void setChildObject(String childObject) {
        this.childObject = childObject;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getFirstTime() {
        return firstTime;
    }

    public void setFirstTime(String firstTime) {
        this.firstTime = firstTime;
    }

    public String getLastTime() {
        return lastTime;
    }

    public void setLastTime(String lastTime) {
        this.lastTime = lastTime;
    }

    public EventDefinition(String message, String resource, String childObject, int count, String firstTime, String lastTime) {
        this.message = message;
        this.resource = resource;
        this.childObject = childObject;
        this.count = count;
        this.firstTime = firstTime;
        this.lastTime = lastTime;
    }

    public EventDefinition() {
    }
}

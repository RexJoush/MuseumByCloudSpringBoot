package com.nwu.entity.cluster.Definition;

/**
 * @author Rex Joush
 * @time 2021.04.18
 */
/**
 * 集群信息的 namespace 实体类
 */
public class NamespaceDefinition {

    private boolean status;  // 状态
    private String name;    // 命名空间名称
    private String phase;   // 运行阶段
    private String time;    // 创建时间

    @Override
    public String toString() {
        return "NamespaceDefinition{" +
                "status='" + status + '\'' +
                ", name='" + name + '\'' +
                ", phase='" + phase + '\'' +
                ", time='" + time + '\'' +
                '}';
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhase() {
        return phase;
    }

    public void setPhase(String phase) {
        this.phase = phase;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public NamespaceDefinition(boolean status, String name, String phase, String time) {
        this.status = status;
        this.name = name;
        this.phase = phase;
        this.time = time;
    }

    public NamespaceDefinition() {
    }
}

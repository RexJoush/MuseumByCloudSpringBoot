package com.nwu.entity.cluster.Definition;

/**
 * @author Rex Joush
 * @time 2021.04.18
 */

/**
 * 集群信息的 cluster role 实体类
 */
public class ClusterRoleDefinition {

    private String name; // 集群角色名称
    private String time; // 创建时间

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "ClusterRoleDefinition{" +
                "name='" + name + '\'' +
                ", time='" + time + '\'' +
                '}';
    }

    public ClusterRoleDefinition() {
    }

    public ClusterRoleDefinition(String name, String time) {
        this.name = name;
        this.time = time;
    }
}

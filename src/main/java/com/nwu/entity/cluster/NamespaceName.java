package com.nwu.entity.cluster;

/**
 * @author Rex Joush
 * @time 2021.03.31
 */

/**
 * 命名空间名称的实体类，用于选择
 */
public class NamespaceName {

    private String label; // 标签名
    private String value; // 标签值

    @Override
    public String toString() {
        return "NamespaceName{" +
                "label='" + label + '\'' +
                ", value='" + value + '\'' +
                '}';
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public NamespaceName() {
    }

    public NamespaceName(String label, String value) {
        this.label = label;
        this.value = value;
    }
}

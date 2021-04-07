package com.nwu.entity.workload;

/**
 * @author Rex Joush
 * @time 2021.04.04
 */

/**
 * cpu 和 内存的利用率
 */
public class Usage {

    private String cpu;
    private String memory;

    @Override
    public String toString() {
        return "Usage{" +
                "cpu='" + cpu + '\'' +
                ", memory='" + memory + '\'' +
                '}';
    }

    public String getCpu() {
        return cpu;
    }

    public void setCpu(String cpu) {
        this.cpu = cpu;
    }

    public String getMemory() {
        return memory;
    }

    public void setMemory(String memory) {
        this.memory = memory;
    }

    public Usage(String cpu, String memory) {
        this.cpu = cpu;
        this.memory = memory;
    }

    public Usage() {
    }
}

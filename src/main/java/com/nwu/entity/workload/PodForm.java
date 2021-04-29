package com.nwu.entity.workload;

import jdk.jfr.DataAmount;

import java.util.Map;

/**
 * @author zqy
 * @time 2021.04.27
 */

public class PodForm {
    private String name;//Pod名称 String
    private String namespace;//Pod命名空间 String
    private Map<String, String> labels;//Pod标签 Map<String, String>
    private Map<String, String> annotations;//Pod描述信息 Map<String, String>
    private String secretName;//下载Pod所用镜像需要的secret名称 String
    private String image;//Pod里容器镜像 String
    private String imagePullPolicy;//Pod里容器镜像下载策略 String
    private String[] commands;//Pod里容器启动后执行的命令列表 String[]
    private String[] args;//Pod里容器的启动命令参数列表 String[]
    private String cpuLimit;//Pod里容器Cpu的限制，单位为core数 String
    private String cpuRequest;//Pod里容器Cpu请求，容器启动的初始可用数量 String
    private String memoryLimit;//Pod里容内存限制，单位可能为Mib/Gib或者Mi/Gi  String
    private String memoryRequest;//Pod里容器内存请求,容器启动的初始可用数量 String
    private Map<String, String> envVar;//Pod里容器环境变量名称和值 Map<String, String>
    private Integer amount;//Pod副本数量 Integer

    public PodForm(String name, String namespace, Map<String, String> labels, Map<String, String> annotations, String secretName, String image, String imagePullPolicy, String[] commands, String[] args, String cpuLimit, String cpuRequest, String memoryLimit, String memoryRequest, Map<String, String> envVar, Integer amount) {
        this.name = name;
        this.namespace = namespace;
        this.labels = labels;
        this.annotations = annotations;
        this.secretName = secretName;
        this.image = image;
        this.imagePullPolicy = imagePullPolicy;
        this.commands = commands;
        this.args = args;
        this.cpuLimit = cpuLimit;
        this.cpuRequest = cpuRequest;
        this.memoryLimit = memoryLimit;
        this.memoryRequest = memoryRequest;
        this.envVar = envVar;
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public Map<String, String> getLabels() {
        return labels;
    }

    public void setLabels(Map<String, String> labels) {
        this.labels = labels;
    }

    public Map<String, String> getAnnotations() {
        return annotations;
    }

    public void setAnnotations(Map<String, String> annotations) {
        this.annotations = annotations;
    }

    public String getSecretName() {
        return secretName;
    }

    public void setSecretName(String secretName) {
        this.secretName = secretName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImagePullPolicy() {
        return imagePullPolicy;
    }

    public void setImagePullPolicy(String imagePullPolicy) {
        this.imagePullPolicy = imagePullPolicy;
    }

    public String[] getCommands() {
        return commands;
    }

    public void setCommands(String[] commands) {
        this.commands = commands;
    }

    public String[] getArgs() {
        return args;
    }

    public void setArgs(String[] args) {
        this.args = args;
    }

    public String getCpuLimit() {
        return cpuLimit;
    }

    public void setCpuLimit(String cpuLimit) {
        this.cpuLimit = cpuLimit;
    }

    public String getCpuRequest() {
        return cpuRequest;
    }

    public void setCpuRequest(String cpuRequest) {
        this.cpuRequest = cpuRequest;
    }

    public String getMemoryLimit() {
        return memoryLimit;
    }

    public void setMemoryLimit(String memoryLimit) {
        this.memoryLimit = memoryLimit;
    }

    public String getMemoryRequest() {
        return memoryRequest;
    }

    public void setMemoryRequest(String memoryRequest) {
        this.memoryRequest = memoryRequest;
    }

    public Map<String, String> getEnvVar() {
        return envVar;
    }

    public void setEnvVar(Map<String, String> envVar) {
        this.envVar = envVar;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }
}

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

    private String[] labelsKeys;//前端传过来的数据，需要处理成Map
    private String[] labelsValues;//前端传过来的数据，需要处理成Map

    private String[] annotationsKeys;//前端传过来的数据，需要处理成Map
    private String[] annotationsValues;//前端传过来的数据，需要处理成Map

    private String secretName;//下载Pod所用镜像需要的secret名称 String
    private String image;//Pod里容器镜像 String
    private String imagePullPolicy;//Pod里容器镜像下载策略 String
    private String[] commands;//Pod里容器启动后执行的命令列表 String[]
    private String[] args;//Pod里容器的启动命令参数列表 String[]
    private String cpuLimit;//Pod里容器Cpu的限制，单位为core数 String
    private String cpuRequest;//Pod里容器Cpu请求，容器启动的初始可用数量 String
    private String memoryLimit;//Pod里容内存限制，单位可能为Mib/Gib或者Mi/Gi  String
    private String memoryRequest;//Pod里容器内存请求,容器启动的初始可用数量 String

    private String[] envKeys;//前端传过来的数据，需要处理成Map
    private String[] envValues;//前端传过来的数据，需要处理成Map

    private Integer amount;//Pod副本数量 Integer

    public PodForm(String name, String namespace, String[] labelsKeys, String[] labelsValues, String[] annotationsKeys, String[] annotationsValues, String secretName, String image, String imagePullPolicy, String[] commands, String[] args, String cpuLimit, String cpuRequest, String memoryLimit, String memoryRequest, String[] envVarKeys, String[] envVarValues, Integer amount) {
        this.name = name;
        this.namespace = namespace;
        this.labelsKeys = labelsKeys;
        this.labelsValues = labelsValues;
        this.annotationsKeys = annotationsKeys;
        this.annotationsValues = annotationsValues;
        this.secretName = secretName;
        this.image = image;
        this.imagePullPolicy = imagePullPolicy;
        this.commands = commands;
        this.args = args;
        this.cpuLimit = cpuLimit;
        this.cpuRequest = cpuRequest;
        this.memoryLimit = memoryLimit;
        this.memoryRequest = memoryRequest;
        this.envKeys = envVarKeys;
        this.envValues = envVarValues;
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

    public String[] getLabelsKeys() {
        return labelsKeys;
    }

    public void setLabelsKeys(String[] labelsKeys) {
        this.labelsKeys = labelsKeys;
    }

    public String[] getLabelsValues() {
        return labelsValues;
    }

    public void setLabelsValues(String[] labelsValues) {
        this.labelsValues = labelsValues;
    }

    public String[] getAnnotationsKeys() {
        return annotationsKeys;
    }

    public void setAnnotationsKeys(String[] annotationsKeys) {
        this.annotationsKeys = annotationsKeys;
    }

    public String[] getAnnotationsValues() {
        return annotationsValues;
    }

    public void setAnnotationsValues(String[] annotationsValues) {
        this.annotationsValues = annotationsValues;
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

    public String[] getEnvKeys() {
        return envKeys;
    }

    public void setEnvKeys(String[] envKeys) {
        this.envKeys = envKeys;
    }

    public String[] getEnvValues() {
        return envValues;
    }

    public void setEnvValues(String[] envValues) {
        this.envValues = envValues;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }
}

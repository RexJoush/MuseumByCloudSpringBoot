package com.nwu.service.workload.impl;

import com.nwu.dao.workload.PodUsageDao;
import com.nwu.entity.workload.PodUsage;
import com.nwu.service.workload.PodsService;
import com.nwu.util.KubernetesUtils;
import com.nwu.util.TimeUtils;
import io.fabric8.kubernetes.api.model.*;
import io.fabric8.kubernetes.api.model.batch.Job;
import io.fabric8.kubernetes.api.model.metrics.v1beta1.PodMetrics;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;

import static com.nwu.util.GetYamlInputStream.byPath;

/**
 * @author Rex Joush
 * @time 2021.03.22
 */

/**
 *  Pods 的 service 层实现类
 */
@Service
public class PodsServiceImpl implements PodsService {

    @Resource
    private PodUsageDao podUsageDao;

    /**
     * 封装获取的 pod 列表，包含利用率信息和 pod 信息
     * @return 封装好的列表
     */
    public List<Map<String, Object>> formatPodList(List<Pod> items){

        // 返回结果列表
        List<Map<String, Object>> result = new ArrayList<>();

        // 封装 pod 列表
        for (Pod item : items) {
            // 获取最近一条数据
            PodUsage usage = podUsageDao.findLast(item.getMetadata().getName(), item.getMetadata().getNamespace());
            Map<String, Object> map = new HashMap<>();

            // 当前 pod 有利用率信息
            if (usage != null){
                // 放入节点信息
                map.put("pod", item);
                // 放入利用率信息
                map.put("usage",usage);
            } else {
                map.put("pod", item);
                map.put("usage", null);
            }


            // 添加进结果集合
            result.add(map);
        }

        return result;
    }

    @Override
    public List<Map<String, Object>> findAllPods(){

        // 获取当前 pod 节点信息
        List<Pod> items = KubernetesUtils.client.pods().inAnyNamespace().list().getItems();
        // 封装返回结果
        return formatPodList(items);

    }

    @Override
    public List<Map<String, Object>> findPodsByNamespace(String namespace) {

        // 获取当前 pod 节点信息
        List<Pod> items = KubernetesUtils.client.pods().inNamespace(namespace).list().getItems();

        return formatPodList(items);
    }

    @Override
    public List<Map<String, Object>> findPodsByNode(String nodeName) {

        // 获取当前 pod 节点信息
        List<Pod> items = KubernetesUtils.client.pods().inAnyNamespace().withField("spec.nodeName", nodeName).list().getItems();

        return formatPodList(items);
    }

    @Override
    public Pod getPodByNameAndNamespace(String name, String namespace){
        Pod items = KubernetesUtils.client.pods().inNamespace(namespace).withName(name).get();
        return items;
    }

    /**
     * 获取 pod 的利用率情况，并存储数据库
     * @throws InterruptedException
     */
    @Async
    @Override
    public void savePodUsage() throws InterruptedException {

        while (true) {
            List<PodMetrics> items = KubernetesUtils.client.top().pods().metrics().getItems();
            for (PodMetrics item : items) {

                if (item.getContainers().size() != 0) {
                    PodUsage podUsage = new PodUsage();

                    // 设置 pod 名称
                    podUsage.setPodName(item.getMetadata().getName());

                    // 设置命名空间
                    podUsage.setNamespace(item.getMetadata().getNamespace());

                    // 设置 cpu 使用数
                    podUsage.setCpu(item.getContainers().get(0).getUsage().get("cpu").getAmount());

                    // 设置内存使用数
                    podUsage.setMemory(item.getContainers().get(0).getUsage().get("memory").getAmount());

                    // 设置时间
                    podUsage.setTime(TimeUtils.sdf.format(new Date()));

                    podUsageDao.addPodUsage(podUsage);
                }

            }

            // 每隔 60 秒保存一次
            Thread.sleep(1000*60);
        }
    }

    @Override
    public Boolean deletePodByNameAndNamespace(String name, String namespace){

        return KubernetesUtils.client.pods().inNamespace(namespace).withName(name).delete();
    }

    @Override
    public Pod loadPodFromYaml(String path) throws FileNotFoundException {

        InputStream yamlInputStream = byPath(path);

        return KubernetesUtils.client.pods().load(yamlInputStream).get();
    }

    @Override
    public Pod createPodByYaml(String path) throws FileNotFoundException {

        InputStream yamlInputStream = byPath(path);

        Pod pod = KubernetesUtils.client.pods().load(yamlInputStream).get();
        String nameSpace = pod.getMetadata().getNamespace();
        try {
            pod = KubernetesUtils.client.pods().inNamespace(nameSpace).create(pod);
        }catch(Exception e){
            System.out.println("缺少必要的命名空间参数，或是已经有相同的资源对象，在PodsServiceImpl类的createPodByYaml方法");
        }
        return pod;
    }

    @Override
    public Pod createOrReplacePod(String path) throws FileNotFoundException {

        InputStream yamlInputStream = byPath(path);

        Pod pod = KubernetesUtils.client.pods().load(yamlInputStream).get();
        String nameSpace = pod.getMetadata().getNamespace();

        try {
            pod = KubernetesUtils.client.pods().inNamespace(nameSpace).createOrReplace(pod);
        }catch(Exception e){
            System.out.println("缺少必要的命名空间参数，或是已经有相同的资源对象，在PodsServiceImpl类的createOrReplacePod方法");
        }
        return pod;
    }

    @Override
    public String getPodLogByNameAndNamespace(String name, String namespace){
        String log = "";
        try{
            log = KubernetesUtils.client.pods().inNamespace(namespace).withName(name).getLog();
        }catch(Exception e){
            System.out.println("未获取到Pod的日志");
        }

        return log;
    }

    // name image amount  Service annotation label:key-value namespace
    // imagePullSecret minCPURequirement minMemoryRequirement
    //command
    @Override
    public List<Pod> createPodFromForm(String name, String namespace, Map<String, String> labels, Map<String, String> annotations,
                         String secretName, String images, String imagePullPolicy, String[] command, String[] args,
                         String cpuLimit, String cpuRequest, String memoryLimit, String memoryRequest, Map<String, String> envVar, Integer amount){

        String generateName = "";
        String containerName = name;
        if(amount > 1) {
            generateName = name;
            name = "";
        }

        LocalObjectReference localObjectReference = new LocalObjectReference();
        localObjectReference.setName(secretName);

        ResourceRequirements resourceRequirements = new ResourceRequirements();
        Quantity cpuLimitQu = new QuantityBuilder().withAmount(cpuLimit).build();
        Quantity cpuRequestQu = new QuantityBuilder().withAmount(cpuRequest).build();
        Quantity memoryLimitQu = new QuantityBuilder().withAmount(memoryLimit).build();
        Quantity memoryRequestQu = new QuantityBuilder().withAmount(memoryRequest).build();

        List<EnvVar> envVarList = new ArrayList<EnvVar>();
        Set<String> keySet = envVar.keySet();

        for (String key : keySet) {
            String value = envVar.get(key);
            EnvVar tmpEnvVar = new EnvVar();
            tmpEnvVar.setName(key);
            tmpEnvVar.setValue(value);
            envVarList.add(tmpEnvVar);
        }

        List<Pod> podList = new ArrayList<Pod>();
        while(amount > 0){
            amount -= 1;
            Pod tmpPod = new PodBuilder()
                    .withNewMetadata()
                            //.withNamespace(namespace)
                        .withGenerateName(generateName)
                        .withName(name)
                        .withLabels(labels)
                        .withAnnotations(annotations)
                    .endMetadata()
                    .withNewSpec()
                        .withImagePullSecrets(localObjectReference)
                        .addNewContainer()
                            .withName(containerName)
                            .withImage(images)
                            .withImagePullPolicy(imagePullPolicy)
                            .withCommand(command)
                            .withArgs(args)
                            .withNewResources()
                                .addToLimits("cpu", cpuLimitQu).addToRequests("cpu", cpuRequestQu)
                                .addToLimits("memory", memoryLimitQu).addToRequests("memory", memoryRequestQu)
                            .endResources()
                            .withEnv(envVarList)
                            //addNewPort().withContainerPort(80).endPort()
                        .endContainer()
                    .endSpec().build();
            Pod pod = KubernetesUtils.client.pods().inNamespace(namespace).create(tmpPod);
            podList.add(pod);
        }
        return podList;
    }



}

package com.nwu.service.workload.impl;

import com.nwu.dao.workload.PodUsageDao;
import com.nwu.entity.workload.PodDefinition;
import com.nwu.entity.workload.PodDetails;
import com.nwu.entity.workload.PodForm;
import com.nwu.entity.workload.PodUsage;
import com.nwu.service.workload.PodsService;
import com.nwu.util.KubernetesUtils;
import com.nwu.util.TimeUtils;
import com.nwu.util.format.PodFormat;
import io.fabric8.kubernetes.api.model.*;
import io.fabric8.kubernetes.api.model.metrics.v1beta1.PodMetrics;
import io.kubernetes.client.util.Yaml;
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
 * Pods 的 service 层实现类
 */
@Service
public class PodsServiceImpl implements PodsService {

    @Resource
    private PodUsageDao podUsageDao;

    @Override
    public List<PodDefinition> findAllPods() {

        // 获取当前 pod 节点信息
        List<Pod> items = KubernetesUtils.client.pods().inAnyNamespace().list().getItems();
        // 封装返回结果
        return PodFormat.formatPodList(items);

    }

    @Override
    public List<Pod> findCompletePodsList() {
        List<Pod> items = KubernetesUtils.client.pods().inAnyNamespace().list().getItems();
        return  items;
    }

    @Override
    public List<PodDefinition> findPodsByNamespace(String namespace) {

        // 获取当前 pod 节点信息
        List<Pod> items = KubernetesUtils.client.pods().inNamespace(namespace).list().getItems();

        return PodFormat.formatPodList(items);
    }

    @Override
    public List<PodDefinition> findPodsByNode(String nodeName) {

        // 获取当前 pod 节点信息
        List<Pod> items = KubernetesUtils.client.pods().inAnyNamespace().withField("spec.nodeName", nodeName).list().getItems();
        return PodFormat.formatPodList(items);
    }

    @Override
    public List<PodDefinition> findPodBySvcLabel(String labelKey, String labelValue) {

        // 获取当前 pod 节点信息
        List<Pod> items = KubernetesUtils.client.pods().withLabel(labelKey, labelValue).list().getItems();
        return PodFormat.formatPodList(items);
    }

    @Override
    public List<Pod> findPodsByLabels(Map<String, String> labels) {

        return KubernetesUtils.client.pods().withLabels(labels).list().getItems();
    }

    @Override
    public PodDetails findPodByNameAndNamespace(String name, String namespace) {
        Pod item = KubernetesUtils.client.pods().inNamespace(namespace).withName(name).get();
        List<PodUsage> usages = podUsageDao.findRecentTwenty(name, namespace, TimeUtils.getTwentyMinuteAgo());
        return new PodDetails(item, usages);
    }

    @Override
    public String findPodYamlByNameAndNamespace(String name, String namespace) {
        Pod pod = KubernetesUtils.client.pods().inNamespace(namespace).withName(name).get();
        return Yaml.dump(pod);
    }

    /**
     * 获取 pod 的利用率情况，并存储数据库
     *
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
            Thread.sleep(1000 * 60);
        }
    }

    /**
     * 每天删除一次，两天前的数据
     *
     * @throws InterruptedException
     */
    @Async
    @Override
    public void deletePodUsage() throws InterruptedException {

        // 删除两天前的数据
        while (true) {
            podUsageDao.delTwoDayAgo(TimeUtils.getTwoDayAgo());

            // 每一天删除一次
            Thread.sleep(1000 * 60 * 60 * 24);
        }
    }

    @Override
    public List<PodUsage> findRecentTwenty(String podName, String podNamespace) {
        return null;
    }

    @Override
    public Boolean deletePodByNameAndNamespace(String name, String namespace) {

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
        } catch (Exception e) {
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
        } catch (Exception e) {
            System.out.println("缺少必要的命名空间参数，或是已经有相同的资源对象，在PodsServiceImpl类的createOrReplacePod方法");
        }
        return pod;
    }

    @Override
    public String getPodLogByNameAndNamespace(String name, String namespace) {
        String log = "";
        try {
            log = KubernetesUtils.client.pods().inNamespace(namespace).withName(name).getLog();
        } catch (Exception e) {
            System.out.println("未获取到Pod的日志");
        }

        return log;
    }

    // name image amount  Service annotation label:key-value namespace
    // imagePullSecret minCPURequirement minMemoryRequirement
    //command
    @Override
    public List<Pod> createPodFromForm(PodForm podForm) {

///**
// * 测试的
// */
//
//        Pod testPod = new PodBuilder()
//                    .withNewMetadata()
//                    //.withNamespace(namespace)
////                    .withGenerateName(generateName)
//                    .withName(podform.name)
//                    .withLabels(podform.labels)
//                    .withAnnotations(podform.annotations)
//                    .endMetadata()
//                    .withNewSpec()
////                    .withImagePullSecrets(localObjectReference)
//                    .addNewContainer()
////                    .withName(containerName)
//                    .withImage(podform.image)
////                    .withImagePullPolicy(imagePullPolicy)
//                    .withCommand(podform.command)
//                    .withArgs(podform.args)
//                    .withNewResources()
////                    .addToLimits("cpu", cpuLimitQu).addToRequests("cpu", cpuRequestQu)
////                    .addToLimits("memory", memoryLimitQu).addToRequests("memory", memoryRequestQu)
//                    .endResources()
////                    .withEnv(envVarList)
//                    //addNewPort().withContainerPort(80).endPort()
//                    .endContainer()
//                    .endSpec().build();
//        System.out.println(testPod);
//        testPod = KubernetesUtils.client.pods().create(testPod);
//
//        List<Pod> p = new ArrayList<Pod>();
//        p.add(testPod);
//        return p;
/**
 * 原来的
 */
        String generateName = "";
        String containerName = podForm.getName();
        List<String> name = new ArrayList<String>(Collections.singleton(containerName));
        int amount = podForm.getAmount();
        if (amount > 1) {
            generateName = containerName + '-';
            name.set(0, generateName + 0);
            for(int i = 1; i < amount; i ++){
                name.add(generateName + i);
            }
        }

        LocalObjectReference localObjectReference = new LocalObjectReference();
        localObjectReference.setName(podForm.getSecretName());

        ResourceRequirements resourceRequirements = new ResourceRequirements();
        Quantity cpuLimitQu = new QuantityBuilder().withAmount(podForm.getCpuLimit()).build();
        Quantity cpuRequestQu = new QuantityBuilder().withAmount(podForm.getCpuRequest()).build();
        Quantity memoryLimitQu = new QuantityBuilder().withAmount(podForm.getMemoryLimit()).build();
        Quantity memoryRequestQu = new QuantityBuilder().withAmount(podForm.getMemoryRequest()).build();

        List<EnvVar> envVarList = new ArrayList<EnvVar>();
        Set<String> keySet = podForm.getEnvVar().keySet();

        for (String key : keySet) {
            String value = podForm.getEnvVar().get(key);
            EnvVar tmpEnvVar = new EnvVar();
            tmpEnvVar.setName(key);
            tmpEnvVar.setValue(value);
            envVarList.add(tmpEnvVar);
        }

        List<Pod> podList = new ArrayList<Pod>();
        while (amount > 0) {
            amount -= 1;
            Pod tmpPod = new PodBuilder()
                    .withNewMetadata()
                    //.withNamespace(namespace)
                    .withGenerateName(generateName)
                    .withName(podForm.getName() + '-' + amount)
                    .withLabels(podForm.getLabels())
                    .withAnnotations(podForm.getAnnotations())
                    .endMetadata()
                    .withNewSpec()
                    .withImagePullSecrets(localObjectReference)
                    .addNewContainer()
                    .withName(containerName)
                    .withImage(podForm.getImage())
                    .withImagePullPolicy(podForm.getImagePullPolicy())
//                    .withCommand(podForm.getCommand())
//                    .withArgs(podForm.getArgs())
                    .withNewResources()
                    .addToLimits("cpu", cpuLimitQu).addToRequests("cpu", cpuRequestQu)
                    .addToLimits("memory", memoryLimitQu).addToRequests("memory", memoryRequestQu)
                    .endResources()
                    .withEnv(envVarList)
                    //addNewPort().withContainerPort(80).endPort()
                    .endContainer()
                    .endSpec().build();
            System.out.println(amount);
            System.out.println(tmpPod);
            Pod pod = KubernetesUtils.client.pods().inNamespace(podForm.getNamespace()).createOrReplace(tmpPod);
            podList.add(pod);
        }
        return podList;
    }


}

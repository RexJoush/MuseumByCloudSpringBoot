package com.nwu.service.workload.impl;

import com.nwu.dao.workload.PodUsageDao;
import com.nwu.entity.workload.Pod.PodDefinition;
import com.nwu.entity.workload.Pod.PodDetails;
import com.nwu.entity.workload.Pod.PodForm;
import com.nwu.entity.workload.Pod.PodUsage;
import com.nwu.service.workload.PodsService;
import com.nwu.util.KubernetesUtils;
import com.nwu.util.TimeUtils;
import com.nwu.util.format.PodFormat;
import io.fabric8.kubernetes.api.model.*;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.DeploymentBuilder;
import io.fabric8.kubernetes.api.model.metrics.v1beta1.PodMetrics;
import io.kubernetes.client.util.Yaml;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
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
    public Pair<Integer, Boolean> createOrReplacePodByYamlString(String yaml){
        try{
            Pod pod = Yaml.loadAs(yaml, Pod.class);
            KubernetesUtils.client.pods().inNamespace(pod.getMetadata().getNamespace()).withName(pod.getMetadata().getName()).createOrReplace(pod);
            return Pair.of(1200, true);
        }catch (Exception e){
            System.out.println("创建 Pod 失败，请检查 Yaml 格式或是否重名，在 PodsServiceImpl 类的 createOrReplacePodByYamlString 方法中");
        }
        return Pair.of(1201, null);
    }

    @Override
    public Pod createPodByYamlPath(String path) throws FileNotFoundException {

        InputStream yamlInputStream = byPath(path);

        Pod pod = null;
        try {
            pod = KubernetesUtils.client.pods().load(yamlInputStream).createOrReplace();
        } catch (Exception e) {
            System.out.println("缺少必要的命名空间参数，或是已经有相同的资源对象，在PodsServiceImpl类的createPodByYaml方法");
        }
        return pod;
    }

    @Override
    public int createPodByYamlFile(File yaml) throws FileNotFoundException {

        InputStream yamlInputStream = new FileInputStream(yaml);

        try {
            Pod pod = KubernetesUtils.client.pods().load(yamlInputStream).get();
            yamlInputStream.close();
            System.out.println(pod);
            pod = KubernetesUtils.client.pods().create(pod);
            if(pod != null) return 1200;
            return 1202;
        } catch (Exception e) {
            System.out.println("缺少必要的命名空间参数，或是已经有相同的资源对象，在PodsServiceImpl类的createPodByYaml方法");
        }
        return 1203;
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
    public Pair<Integer, String> getPodLogFromContainer(String name, String namespace, String containerName) {
        try {
            String log = KubernetesUtils.client.pods().inNamespace(namespace).withName(name).inContainer(containerName).getLog();
            return Pair.of(1200, log);
        } catch (Exception e) {
            System.out.println("未获取到Pod的日志");
        }
        return Pair.of(1201, null);
    }

    @Override
    public Pair<Integer, Map<String, String>> getPodAllLogs(String name, String namespace) {
        try {
            Map<String, String> logs = new HashMap<>();
            List<Container> containers = KubernetesUtils.client.pods().inNamespace(namespace).withName(name).get().getSpec().getContainers();
            for(int i = containers.size() - 1; i >= 0 ; i-- ){
                String containerName = containers.get(i).getName();
                String log = KubernetesUtils.client.pods().inNamespace(namespace).withName(name).inContainer(containerName).getLog();
                logs.put(containerName, log);
            }
            return Pair.of(1200, logs);
        } catch (Exception e) {
            System.out.println("未获取到Pod的日志");
        }
        return Pair.of(1201, null);
    }

    @Override
    public Pair<Integer, Map> getAllPodsAllLogs(List<Pod> pods){
        try{
            // 获取每个 Pod 的所有 Logs
            Boolean flag = true;
            Map<String, Map<String, String>> podLogs = new HashMap<>();
            for(int i = 0; i < pods.size(); i++){
                Pair<Integer, Map<String, String>> podAllLogs = this.getPodAllLogs(pods.get(i).getMetadata().getName(), pods.get(i).getMetadata().getNamespace());
                if(podAllLogs.getLeft() != 1200){
                    podLogs.put(pods.get(i).getMetadata().getName(), null);
                    flag = false;
                }
                else
                    podLogs.put(pods.get(i).getMetadata().getName(), podAllLogs.getRight());
            }
            if(flag) return Pair.of(1203, podLogs);
            return Pair.of(1200, podLogs);
        }catch (Exception e){
            System.out.println("操作失败，在 PodsServiceImpl 类的 getAllPodsAllLogs 方法中");
        }
        return Pair.of(1201, null);
    }



    @Override
    public Pair<Integer, Boolean> createPodFromForm(PodForm podForm) {
        try{
            //Name
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

            //Image Secret
            LocalObjectReference localObjectReference = new LocalObjectReference();
            localObjectReference.setName(podForm.getSecretName());

            //CPU Memory
//        ResourceRequirements resourceRequirements = new ResourceRequirements();
            Quantity cpuLimitQu = new Quantity(podForm.getCpuLimit(),podForm.getCpuUnit());
            Quantity cpuRequestQu = new Quantity(podForm.getCpuRequest(), podForm.getCpuUnit());
            Quantity memoryLimitQu = new Quantity(podForm.getMemoryLimit(), podForm.getMemoryUnit());
            Quantity memoryRequestQu = new Quantity(podForm.getMemoryRequest(), podForm.getMemoryUnit());
            System.out.println(podForm.getMemoryLimit() + '=' + podForm.getMemoryUnit() + '|');


            System.out.println(podForm.getMemoryRequest() + '=' + podForm.getMemoryUnit() + '|');


            System.out.println(podForm.getCpuLimit() + '=' + podForm.getCpuUnit() + '|');



            System.out.println(podForm.getCpuRequest() + '=' + podForm.getCpuUnit() + '|');





            //Env
            List<EnvVar> envVarList = new ArrayList<EnvVar>();
            String[] envKeys = podForm.getEnvKeys();
            String envValues[] = podForm.getEnvValues();
            for(int i = 0; i < envKeys.length; i++){
                EnvVar tmpEnvVar = new EnvVar();
                tmpEnvVar.setName(envKeys[i]);
                tmpEnvVar.setValue(envValues[i]);
                envVarList.add(tmpEnvVar);
            }

            //Command
            Boolean commandFlag = false;
            List<String> commandsList = new ArrayList<>();
            String commands[] = podForm.getCommands();
            for(String command : commands){
                if(!("".equals(command)) || command != null){
                    commandsList.add(command);
                    commandFlag = true;
                }
            }

            //Arg
            Boolean argsFlag = false;
            List<String> argsList = new ArrayList<>();
            String args[] = podForm.getArgs();
            for(String arg : args){
                if(!("".equals(arg)) || arg != null){
                    argsList.add(arg);
                    argsFlag = true;
                }
            }

            //labels
            String[] labelsKeys = podForm.getLabelsKeys();
            String[] labelsValues = podForm.getLabelsValues();
            Map<String, String> labels = new HashMap<>();
            for(int i = 0; i < labelsKeys.length; i++)
                labels.put(labelsKeys[i], labelsValues[i]);

            //annotations
            String[] annotationsKeys = podForm.getAnnotationsKeys();
            String[] annotationsValues = podForm.getAnnotationsValues();
            Map<String, String> annotations = new HashMap<>();
            for(int i = 0; i < annotationsKeys.length; i++)
                labels.put(annotationsKeys[i], annotationsValues[i]);

            //NodeSelect
            Map<String, String> nodeSelect = new HashMap<>();
            nodeSelect.put("type", "node");

            List<Pod> podList = new ArrayList<Pod>();
            while (amount > 0) {
                amount -= 1;
                Pod tmpPod = new PodBuilder()
                        .withNewMetadata()
                        //.withNamespace(namespace)
                        .withGenerateName(generateName)
                        .withName(podForm.getName() + '-' + amount)
                        .withLabels(labels)
                        .withAnnotations(annotations)
                        .endMetadata()
                        .withNewSpec()
//                        .withNodeSelector(nodeSelect)
                        .withImagePullSecrets(localObjectReference)
                        .addNewContainer()
                        .withName(containerName)
                        .withImage(podForm.getImage())
                        .withImagePullPolicy(podForm.getImagePullPolicy())
//                    .withCommand()
//                    .withArgs(podForm.getArgs())
                        .withNewResources()
                        .addToLimits("cpu", cpuLimitQu).addToRequests("cpu", cpuRequestQu)
                        .addToLimits("memory", memoryLimitQu).addToRequests("memory", memoryRequestQu)
                        .endResources()
                        .withEnv(envVarList)
                        //addNewPort().withContainerPort(80).endPort()
                        .endContainer()
                        .endSpec().build();
                if(commandFlag) tmpPod.getSpec().getContainers().get(0).setCommand(commandsList);
                if(argsFlag) tmpPod.getSpec().getContainers().get(0).setArgs(argsList);
                try{
                    Pod pod = KubernetesUtils.client.pods().inNamespace(podForm.getNamespace()).createOrReplace(tmpPod);
                    podList.add(pod);
                }catch (Exception e){
                    System.out.println(e);
                    return Pair.of(1201, null);
                }
            }
            return Pair.of(1200, true);
        }catch (Exception e){
            System.out.println("创建 Pod 失败，在 PodsServiceImpl 类的 createPodFromForm 方法");
            System.out.println(e);
        }
        return Pair.of(1201, null);
    }


}

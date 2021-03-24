package com.nwu.service.workload.impl;

import com.nwu.service.workload.PodsService;
import com.nwu.util.KubernetesConfig;
import io.fabric8.kubernetes.api.model.*;
import io.fabric8.kubernetes.api.model.batch.CronJob;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;

import static com.nwu.service.getYamlInputStream.byPath;

/**
 * @author Rex Joush
 * @time 2021.03.22
 */

/**
 *  Pods 的 service 层实现类
 */
@Service
public class PodsServiceImpl implements PodsService {
    public static void main(String[] args) {
        System.out.println(new PodsServiceImpl().findAllPods());
    }


    @Override
    public List<Pod> findAllPods(){

        List<Pod> items = KubernetesConfig.client.pods().inAnyNamespace().list().getItems();

        return items;

    }

    @Override
    public List<Pod> findPodsByNamespace(String namespace) {

        List<Pod> items = KubernetesConfig.client.pods().inNamespace(namespace).list().getItems();

        return items;
    }

    @Override
    public Boolean deletePodByNameAndNamespace(String name, String namespace){

        Boolean delete = KubernetesConfig.client.pods().inNamespace(namespace).withName(name).delete();

        return delete;
    }

    @Override
    public Pod loadPodFromYaml(String path) throws FileNotFoundException {

        InputStream yamlInputStream = byPath(path);

        Pod pod = KubernetesConfig.client.pods().load(yamlInputStream).get();

        return pod;
    }

    @Override
    public Pod createPodByYaml(String path) throws FileNotFoundException {

        InputStream yamlInputStream = byPath(path);

        Pod pod = KubernetesConfig.client.pods().load(yamlInputStream).get();
        String nameSpace = pod.getMetadata().getNamespace();
        try {
            pod = KubernetesConfig.client.pods().inNamespace(nameSpace).create(pod);
        }catch(Exception e){
            System.out.println("缺少必要的命名空间参数，或是已经有相同的资源对象，在PodsServiceImpl类的createPodByYaml方法");
        }
        return pod;
    }

    @Override
    public Pod createOrReplacePod(String path) throws FileNotFoundException {

        InputStream yamlInputStream = byPath(path);

        Pod pod = KubernetesConfig.client.pods().load(yamlInputStream).get();
        String nameSpace = pod.getMetadata().getNamespace();

        try {
            pod = KubernetesConfig.client.pods().inNamespace(nameSpace).createOrReplace(pod);
        }catch(Exception e){
            System.out.println("缺少必要的命名空间参数，或是已经有相同的资源对象，在PodsServiceImpl类的createOrReplacePod方法");
        }
        return pod;
    }

    @Override
    public String getPodLogByNameAndNamespace(String name, String namespace){
        String log = "";
        try{
            log = KubernetesConfig.client.pods().inNamespace(namespace).withName(name).getLog();
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
        Iterator<String> keyIterator = keySet.iterator();
        while(keyIterator.hasNext()){
            String key = (String) keyIterator.next();
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
            Pod pod = KubernetesConfig.client.pods().inNamespace(namespace).create(tmpPod);
            podList.add(pod);
        }
        return podList;
    }

}

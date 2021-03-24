package com.nwu.service.workload.impl;

import com.nwu.service.workload.PodsService;
import com.nwu.util.KubernetesConfig;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.PodBuilder;
import io.fabric8.kubernetes.api.model.batch.CronJob;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

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
    //comm
    @Override
    public Pod createPod(){
        Pod pod = new PodBuilder().withNewMetadata().withName("demo-pod1").endMetadata()
                .withNewSpec()
                .addNewContainer()
                .withName("nginx")
                .withImage("nginx:1.7.9")
                .addNewPort().withContainerPort(80).endPort()
                .endContainer()
                .endSpec()
                .build();
        return pod;
    }

}

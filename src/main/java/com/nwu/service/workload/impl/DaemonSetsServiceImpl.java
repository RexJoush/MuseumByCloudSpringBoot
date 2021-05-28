package com.nwu.service.workload.impl;

import com.nwu.service.workload.DaemonSetsService;
import com.nwu.util.FilterPodsByControllerUid;
import com.nwu.util.KubernetesUtils;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.apps.DaemonSet;
import io.kubernetes.client.openapi.models.V1DaemonSet;
import io.kubernetes.client.util.Yaml;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.nwu.util.GetYamlInputStream.byPath;

/**
 * @author Rex Joush
 * @time 2021.03.22
 */

/**
 * Daemon Sets 的 service 层实现类
 */
@Service
public class DaemonSetsServiceImpl implements DaemonSetsService {
    @Override
    public List<DaemonSet> findAllDaemonSets(){

        List<DaemonSet> items = KubernetesUtils.client.apps().daemonSets().inAnyNamespace().list().getItems();

        return items;

    }

    @Override
    public List<DaemonSet> findDaemonSetsByNamespace(String namespace) {

        List<DaemonSet> items = KubernetesUtils.client.apps().daemonSets().inNamespace(namespace).list().getItems();

        return items;
    }

    @Override
    public Boolean deleteDaemonSetByNameAndNamespace(String name, String namespace){
        Boolean delete = KubernetesUtils.client.apps().daemonSets().inNamespace(namespace).withName(name).delete();
        return delete;
    }

    @Override
    public DaemonSet loadDaemonSetFromYaml(String path) throws FileNotFoundException {

        InputStream yamlInputStream = byPath(path);

        DaemonSet daemonSet = KubernetesUtils.client.apps().daemonSets().load(yamlInputStream).get();
        return daemonSet;
    }

    @Override
    public DaemonSet createOrReplaceDaemonSet(String path) throws FileNotFoundException {
        InputStream yamlInputStream = byPath(path);
        System.out.println(0);
        DaemonSet daemonSet = KubernetesUtils.client.apps().daemonSets().load(yamlInputStream).get();
        String namespace = daemonSet.getMetadata().getNamespace();
        System.out.println(1);
        try {
            daemonSet = KubernetesUtils.client.apps().daemonSets().inNamespace(namespace).createOrReplace(daemonSet);
        }catch(Exception e){
            System.out.println("缺少必要的命名空间参数，或是已经有相同的资源对象，在DaemonSetsServiceImpl类的createOrReplaceDaemonSet方法");
        }
        return daemonSet;
    }

    @Override
    public Boolean createOrReplaceDaemonSetByYaml(String yaml) throws IOException {
        V1DaemonSet load = (V1DaemonSet) Yaml.load(yaml);
        System.out.println("1 + \n" + load);
        DaemonSet daemonSet = KubernetesUtils.client.apps().daemonSets().load(yaml).get();
        String namespace = daemonSet.getMetadata().getNamespace();
        System.out.println(1);
        try {
            KubernetesUtils.client.apps().daemonSets().inNamespace(namespace).createOrReplace(daemonSet);
            return true;
        }catch(Exception e){
            System.out.println("缺少必要的命名空间参数，或是已经有相同的资源对象，在DaemonSetsServiceImpl类的createOrReplaceDaemonSet方法");
            return false;
        }
    }

    @Override
    public DaemonSet getDaemonSetByNameAndNamespace(String name, String namespace){
        DaemonSet item = KubernetesUtils.client.apps().daemonSets().inNamespace(namespace).withName(name).get();
        return item;
    }

    @Override
    public String getDaemonSetYamlByNameAndNamespace(String name ,String namespace){
        DaemonSet item = KubernetesUtils.client.apps().daemonSets().inNamespace(namespace).withName(name).get();
        String s = Yaml.dump(item);
        System.out.println(s);
        return s;
    }

    @Override
    public List<Pod> getPodDaemonSetInvolved(String name, String namespace){
        //获取 DaemonSet
        DaemonSet aDaemonSet = KubernetesUtils.client.apps().daemonSets().inNamespace(namespace).withName(name).get();
        Map<String, String> matchLabels = aDaemonSet.getSpec().getSelector().getMatchLabels();
        String uid = aDaemonSet.getMetadata().getUid();
        List<Pod> pods = new ArrayList<>();
        try{
            //获取 Pods
            PodsServiceImpl podsService = new PodsServiceImpl();
            pods = FilterPodsByControllerUid.filterPodsByControllerUid(uid, podsService.findPodsByLabels(matchLabels));
        }catch (Exception e){
            System.out.println("未获取到相应 Pod");
        }
        return pods;
    }
}

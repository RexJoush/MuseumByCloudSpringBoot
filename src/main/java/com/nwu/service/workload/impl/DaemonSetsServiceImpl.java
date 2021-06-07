package com.nwu.service.workload.impl;

import com.nwu.service.workload.DaemonSetsService;
import com.nwu.util.FilterPodsByControllerUid;
import com.nwu.util.KubernetesUtils;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.apps.DaemonSet;
import io.kubernetes.client.util.Yaml;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
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
        try{
            List<DaemonSet> items = KubernetesUtils.client.apps().daemonSets().inAnyNamespace().list().getItems();
            return items;
        }catch(Exception e){
            System.out.println("获取DaemonSets失败，在DaemonSetsServiceImpl类的findAllDaemonSets方法中");
        }
        return null;
    }

    @Override
    public List<DaemonSet> findDaemonSetsByNamespace(String namespace) {
        try{
            List<DaemonSet> items = KubernetesUtils.client.apps().daemonSets().inNamespace(namespace).list().getItems();
            return items;
        }catch(Exception e){
            System.out.println("获取DaemonSets失败，在DaemonSetsServiceImpl类的findDaemonSetsByNamespace方法中");
        }
        return  null;
    }

    @Override
    public Boolean deleteDaemonSetByNameAndNamespace(String name, String namespace){
        try{
            Boolean delete = KubernetesUtils.client.apps().daemonSets().inNamespace(namespace).withName(name).delete();
            return delete;
        }catch(Exception e){
            System.out.println("删除DaemonSet失败，在DaemonSetsServiceImpl类的deleteDaemonSetByNameAndNamespace方法中");
        }
        return null;
    }

    @Override
    public DaemonSet loadDaemonSetFromYaml(String path) throws FileNotFoundException {
        try{
            InputStream yamlInputStream = byPath(path);
            DaemonSet daemonSet = KubernetesUtils.client.apps().daemonSets().load(yamlInputStream).get();
            return daemonSet;
        }catch (Exception e){
            System.out.println("加载DaemonSet失败，在DaemonSetsServiceImpl类的loadDaemonSetFromYaml方法中");
        }
        return null;
    }

    @Override
    public DaemonSet createOrReplaceDaemonSet(String path) throws FileNotFoundException {
        InputStream yamlInputStream = byPath(path);
        try {
            DaemonSet daemonSet = KubernetesUtils.client.apps().daemonSets().load(yamlInputStream).get();
            String namespace = daemonSet.getMetadata().getNamespace();
            daemonSet = KubernetesUtils.client.apps().daemonSets().inNamespace(namespace).createOrReplace(daemonSet);
            return daemonSet;
        }catch(Exception e){
            System.out.println("获取DaemonSet失败，缺少必要的命名空间参数，或是已经有相同的资源对象，在DaemonSetsServiceImpl类的createOrReplaceDaemonSet方法");
        }
        return null;
    }

    @Override
    public Boolean createOrReplaceDaemonSetByYaml(String yaml) {
        try {
            DaemonSet daemonSet = KubernetesUtils.client.apps().daemonSets().load(yaml).get();
            String namespace = daemonSet.getMetadata().getNamespace();
            KubernetesUtils.client.apps().daemonSets().inNamespace(namespace).createOrReplace(daemonSet);
            return true;
        }catch(Exception e){
            System.out.println("获取DaemonSet失败，缺少必要的命名空间参数，或是已经有相同的资源对象，在DaemonSetsServiceImpl类的createOrReplaceDaemonSet方法");
        }
        return null;
    }

    @Override
    public DaemonSet getDaemonSetByNameAndNamespace(String name, String namespace){
        try{
            DaemonSet item = KubernetesUtils.client.apps().daemonSets().inNamespace(namespace).withName(name).get();
            return item;
        }catch (Exception e){
            System.out.println("获取DaemonSet失败，在DaemonSetsServiceImpl类的getDaemonSetByNameAndNamespace方法中");
        }
        return null;
    }

    @Override
    public String getDaemonSetYamlByNameAndNamespace(String name ,String namespace){
        try{
            DaemonSet item = KubernetesUtils.client.apps().daemonSets().inNamespace(namespace).withName(name).get();
            String s = Yaml.dump(item);
            return s;
        }catch(Exception e){
            System.out.println("获取Yaml失败，在DaemonSetsServiceImpl类的getDaemonSetYamlByNameAndNamespace方法中");
        }
        return null;
    }

    @Override
    public List<Pod> getPodDaemonSetInvolved(String name, String namespace){
        try{
            //获取 DaemonSet
            DaemonSet aDaemonSet = KubernetesUtils.client.apps().daemonSets().inNamespace(namespace).withName(name).get();
            Map<String, String> matchLabels = aDaemonSet.getSpec().getSelector().getMatchLabels();
            String uid = aDaemonSet.getMetadata().getUid();
            List<Pod> pods = new ArrayList<>();
            //获取 Pods
            PodsServiceImpl podsService = new PodsServiceImpl();
            pods = FilterPodsByControllerUid.filterPodsByControllerUid(uid, podsService.findPodsByLabels(matchLabels));
            return pods;
        }catch (Exception e){
            System.out.println("获取Resources失败，未获取到相应，在DaemonSetsServiceImpl类的getPodDaemonSetInvolved方法中");
        }
        return null;
    }
}

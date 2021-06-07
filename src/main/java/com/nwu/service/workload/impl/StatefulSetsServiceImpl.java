package com.nwu.service.workload.impl;

import com.nwu.service.workload.StatefulSetsService;
import com.nwu.util.FilterPodsByControllerUid;
import com.nwu.util.KubernetesUtils;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.apps.StatefulSet;
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
 * Stateful Sets 的 service 层实现类
 */
@Service
public class StatefulSetsServiceImpl implements StatefulSetsService {
    @Override
    public List<StatefulSet> findAllStatefulSets(){
        try{
            List<StatefulSet> items = KubernetesUtils.client.apps().statefulSets().inAnyNamespace().list().getItems();

            return items;
        }catch(Exception e){
            System.out.println("获取StatefulSets失败，在StatefulSetsServiceImpl类的findAllStatefulSets方法中");
        }
        return null;


    }

    @Override
    public List<StatefulSet> findStatefulSetsByNamespace(String namespace) {
        try{
            List<StatefulSet> items = KubernetesUtils.client.apps().statefulSets().inNamespace(namespace).list().getItems();

            return items;
        }catch(Exception e){
            System.out.println("获取StatefulSets失败，在StatefulSetsServiceImpl类的findStatefulSetsByNamespace方法中");
        }
        return null;

    }

    @Override
    public StatefulSet getStatefulSetByNameAndNamespace(String name, String namespace){
        try{
            StatefulSet item = KubernetesUtils.client.apps().statefulSets().inNamespace(namespace).withName(name).get();
            return item;
        }catch(Exception e){
            System.out.println("获取StatefulSet失败，在StatefulSetsServiceImpl类的getStatefulSetByNameAndNamespace方法中");
        }
        return null;

    }

    @Override
    public Boolean deleteStatefulSetByNameAndNamespace(String name, String namespace){
        try{
            Boolean delete = KubernetesUtils.client.apps().statefulSets().inNamespace(namespace).withName(name).delete();

            return delete;
        }catch(Exception e){
            System.out.println("删除StatefulSet失败，在StatefulSetsServiceImpl类的deleteStatefulSetByNameAndNamespace方法中");
        }
        return null;

    }

    @Override
    public StatefulSet loadStatefulSetFromYaml(String path) throws FileNotFoundException {

        InputStream yamlInputStream = byPath(path);
        try{
            StatefulSet statefulSet = KubernetesUtils.client.apps().statefulSets().load(yamlInputStream).get();

            return statefulSet;
        }catch(Exception e){
            System.out.println("加载StatefulSet失败，在StatefulSetsServiceImpl类的loadStatefulSetFromYaml方法中");
        }
        return null;

    }

    @Override
    public StatefulSet createStatefulSetByYaml(String path) throws FileNotFoundException {

        InputStream yamlInputStream = byPath(path);

        try {
            StatefulSet statefulSet = KubernetesUtils.client.apps().statefulSets().load(yamlInputStream).get();
            String nameSpace = statefulSet.getMetadata().getNamespace();
            statefulSet = KubernetesUtils.client.apps().statefulSets().inNamespace(nameSpace).create(statefulSet);
            return statefulSet;
        }catch(Exception e){
            System.out.println("创建StatefulSet失败，缺少必要的命名空间参数，或是已经有相同的资源对象，在StatefulSetsServiceImpl类的createStatefulSetByYaml方法");
        }
        return null;
    }

    @Override
    public StatefulSet createOrReplaceStatefulSet(String path) throws FileNotFoundException {

        InputStream yamlInputStream = byPath(path);


        try {
            StatefulSet statefulSet = KubernetesUtils.client.apps().statefulSets().load(yamlInputStream).get();
            String nameSpace = statefulSet.getMetadata().getNamespace();
            statefulSet = KubernetesUtils.client.apps().statefulSets().inNamespace(nameSpace).createOrReplace(statefulSet);
            return statefulSet;
        }catch(Exception e){
            System.out.println("创建StatefulSet失败，缺少必要的命名空间参数，或是已经有相同的资源对象，在StatefulSetsServiceImpl类的createOrReplaceStatefulSet方法");
        }
        return null;
    }

    @Override
    public String getStatefulSetYamlByNameAndNamespace(String name ,String namespace){
        try{
            StatefulSet item = KubernetesUtils.client.apps().statefulSets().inNamespace(namespace).withName(name).get();
            return Yaml.dump(item);
        }catch(Exception e){
            System.out.println("获取Yaml失败，在StatefulSetsServiceImpl类的getStatefulSetYamlByNameAndNamespace方法中");
        }
        return null;

    }

    @Override
    public List<Pod> getPodStatefulSetInvolved(String name, String namespace){
        List<Pod> pods = new ArrayList<>();
        try{
            //获取 StatefulSet
            StatefulSet aStatefulSet = KubernetesUtils.client.apps().statefulSets().inNamespace(namespace).withName(name).get();
            Map<String, String> matchLabels = aStatefulSet.getSpec().getSelector().getMatchLabels();
            String uid = aStatefulSet.getMetadata().getUid();
            //获取 Pods
            PodsServiceImpl podsService = new PodsServiceImpl();
            pods = FilterPodsByControllerUid.filterPodsByControllerUid(uid, podsService.findPodsByLabels(matchLabels));
        }catch (Exception e){
            System.out.println("获取Resources失败，在StatefulSetsServiceImpl类的getPodStatefulSetInvolved方法中");
        }
        return pods;
    }
}

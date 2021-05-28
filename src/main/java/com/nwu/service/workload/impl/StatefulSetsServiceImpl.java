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

        List<StatefulSet> items = KubernetesUtils.client.apps().statefulSets().inAnyNamespace().list().getItems();

        return items;

    }

    @Override
    public List<StatefulSet> findStatefulSetsByNamespace(String namespace) {

        List<StatefulSet> items = KubernetesUtils.client.apps().statefulSets().inNamespace(namespace).list().getItems();

        return items;
    }

    @Override
    public StatefulSet getStatefulSetByNameAndNamespace(String name, String namespace){
        StatefulSet item = KubernetesUtils.client.apps().statefulSets().inNamespace(namespace).withName(name).get();
        return item;
    }

    @Override
    public Boolean deleteStatefulSetByNameAndNamespace(String name, String namespace){

        Boolean delete = KubernetesUtils.client.apps().statefulSets().inNamespace(namespace).withName(name).delete();

        return delete;
    }

    @Override
    public StatefulSet loadStatefulSetFromYaml(String path) throws FileNotFoundException {

        InputStream yamlInputStream = byPath(path);

        StatefulSet statefulSet = KubernetesUtils.client.apps().statefulSets().load(yamlInputStream).get();

        return statefulSet;
    }

    @Override
    public StatefulSet createStatefulSetByYaml(String path) throws FileNotFoundException {

        InputStream yamlInputStream = byPath(path);

        StatefulSet statefulSet = KubernetesUtils.client.apps().statefulSets().load(yamlInputStream).get();
        String nameSpace = statefulSet.getMetadata().getNamespace();
        try {
            statefulSet = KubernetesUtils.client.apps().statefulSets().inNamespace(nameSpace).create(statefulSet);
        }catch(Exception e){
            System.out.println("缺少必要的命名空间参数，或是已经有相同的资源对象，在StatefulSetsServiceImpl类的createStatefulSetByYaml方法");
        }
        return statefulSet;
    }

    @Override
    public StatefulSet createOrReplaceStatefulSet(String path) throws FileNotFoundException {

        InputStream yamlInputStream = byPath(path);

        StatefulSet statefulSet = KubernetesUtils.client.apps().statefulSets().load(yamlInputStream).get();
        String nameSpace = statefulSet.getMetadata().getNamespace();

        try {
            statefulSet = KubernetesUtils.client.apps().statefulSets().inNamespace(nameSpace).createOrReplace(statefulSet);
        }catch(Exception e){
            System.out.println("缺少必要的命名空间参数，或是已经有相同的资源对象，在StatefulSetsServiceImpl类的createOrReplaceStatefulSet方法");
        }
        return statefulSet;
    }

    @Override
    public String getStatefulSetYamlByNameAndNamespace(String name ,String namespace){
        StatefulSet item = KubernetesUtils.client.apps().statefulSets().inNamespace(namespace).withName(name).get();
        return Yaml.dump(item);
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
            System.out.println("未获取到相应 Pod");
        }
        return pods;
    }
}

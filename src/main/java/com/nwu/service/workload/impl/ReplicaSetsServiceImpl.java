package com.nwu.service.workload.impl;

import com.nwu.service.workload.ReplicaSetsService;
import com.nwu.util.FilterPodsByControllerUid;
import com.nwu.util.KubernetesUtils;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.apps.ReplicaSet;
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
 * Replica Sets 的 service 层实现类
 */
@Service
public class ReplicaSetsServiceImpl implements ReplicaSetsService {

    @Override
    public List<ReplicaSet> findAllReplicaSets(){

        List<ReplicaSet> items = KubernetesUtils.client.apps().replicaSets().inAnyNamespace().list().getItems();

        return items;

    }

    @Override
    public List<ReplicaSet> findReplicaSetsByNamespace(String namespace) {

        List<ReplicaSet> items = KubernetesUtils.client.apps().replicaSets().inNamespace(namespace).list().getItems();

        return items;
    }

    @Override
    public ReplicaSet getReplicaSetByNameAndNamespace(String name, String namespace){
        ReplicaSet items = KubernetesUtils.client.apps().replicaSets().inNamespace(namespace).withName(name).get();
        return items;
    }

    @Override
    public Boolean deleteReplicaSetByNameAndNamespace(String name, String namespace){

        Boolean delete = KubernetesUtils.client.apps().replicaSets().inNamespace(namespace).withName(name).delete();

        return delete;
    }

    @Override
    public ReplicaSet loadReplicaSetFromYaml(String path) throws FileNotFoundException {

        InputStream yamlInputStream = byPath(path);

        ReplicaSet replicaSet = KubernetesUtils.client.apps().replicaSets().load(yamlInputStream).get();

        return replicaSet;
    }

    @Override
    public ReplicaSet createReplicaSetByYaml(String path) throws FileNotFoundException {

        InputStream yamlInputStream = byPath(path);

        ReplicaSet replicaSet = KubernetesUtils.client.apps().replicaSets().load(yamlInputStream).get();
        String nameSpace = replicaSet.getMetadata().getNamespace();
        System.out.println(nameSpace);
        try{
            replicaSet = KubernetesUtils.client.apps().replicaSets().inNamespace(nameSpace).create(replicaSet);
        }catch(Exception e){
            System.out.println("缺少必要的命名空间参数，或是已经有相同的资源对象，在ReplicaSetsServiceImpl类的createReplicaSetByYaml方法");
        }

        return replicaSet;
    }

    @Override
    public ReplicaSet createOrReplaceReplicaSet(String path) throws FileNotFoundException {

        InputStream yamlInputStream = byPath(path);

        ReplicaSet replicaSet = KubernetesUtils.client.apps().replicaSets().load(yamlInputStream).get();
        String nameSpace = replicaSet.getMetadata().getNamespace();

        try {
            replicaSet = KubernetesUtils.client.apps().replicaSets().inNamespace(nameSpace).createOrReplace(replicaSet);
        }catch(Exception e){
            System.out.println("缺少必要的命名空间参数，或是已经有相同的资源对象，在ReplicaSetsServiceImpl类的createOrReplaceReplicaSet方法");
        }
        return replicaSet;
    }

    @Override
    public void setReplicas(String name, String namespace, Integer replicas){
        try {
            KubernetesUtils.client.apps().replicaSets().inNamespace(namespace).withName(name).edit().getSpec().setReplicas(replicas);
        }catch (Exception e){
            System.out.println("设置ReplicaSet的replicas失败");
        }
    }

    @Override
    public String getReplicaSetYamlByNameAndNamespace(String name ,String namespace){
        ReplicaSet item = KubernetesUtils.client.apps().replicaSets().inNamespace(namespace).withName(name).get();
        return Yaml.dump(item);
    }

    @Override
    public List<Pod> getPodReplicaSetInvolved(String name, String namespace){
        List<Pod> pods = new ArrayList<>();
        try{
            //获取 ReplicaSet
            ReplicaSet aReplicaSet = KubernetesUtils.client.apps().replicaSets().inNamespace(namespace).withName(name).get();
            Map<String, String> matchLabels = aReplicaSet.getSpec().getSelector().getMatchLabels();
            String uid = aReplicaSet.getMetadata().getUid();
            //获取 Pods
            PodsServiceImpl podsService = new PodsServiceImpl();
            pods = FilterPodsByControllerUid.filterPodsByControllerUid(uid, podsService.findPodsByLabels(matchLabels));
        }catch (Exception e){
            System.out.println("未获取到相应 Pod");
        }
        return pods;
    }
}

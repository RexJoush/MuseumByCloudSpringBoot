package com.nwu.service.workload.impl;

import com.nwu.service.workload.ReplicaSetsService;
import com.nwu.util.FilterPodsByControllerUid;
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
import static com.nwu.util.KubernetesUtils.client;

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
        try{
            List<ReplicaSet> items = client.apps().replicaSets().inAnyNamespace().list().getItems();

            return items;
        }catch(Exception e){
            System.out.println("获取ReplicaSets失败，在ReplicaSetsServiceImpl类的findAllReplicaSets方法中");
        }
        return null;


    }

    @Override
    public List<ReplicaSet> findReplicaSetsByNamespace(String namespace) {
        try{
            List<ReplicaSet> items = client.apps().replicaSets().inNamespace(namespace).list().getItems();

            return items;
        }catch(Exception e){
            System.out.println("获取ReplicaSets失败，在ReplicaSetsServiceImpl类的findReplicaSetsByNamespace方法中");
        }
        return null;

    }

    @Override
    public ReplicaSet getReplicaSetByNameAndNamespace(String name, String namespace){
        try{
            ReplicaSet items = client.apps().replicaSets().inNamespace(namespace).withName(name).get();
            return items;
        }catch(Exception e){
            System.out.println("获取ReplicaSet失败，在ReplicaSetsServiceImpl类的getReplicaSetByNameAndNamespace方法中");
        }
        return null;

    }

    @Override
    public Boolean deleteReplicaSetByNameAndNamespace(String name, String namespace){
        try{
            Boolean delete = client.apps().replicaSets().inNamespace(namespace).withName(name).delete();

            return delete;
        }catch(Exception e){
            System.out.println("删除ReplicaSet失败，在ReplicaSetsServiceImpl类的deleteReplicaSetByNameAndNamespace方法中");
        }
        return null;

    }

    @Override
    public ReplicaSet loadReplicaSetFromYaml(String path) throws FileNotFoundException {
        InputStream yamlInputStream = byPath(path);
        try{
            ReplicaSet replicaSet = client.apps().replicaSets().load(yamlInputStream).get();

            return replicaSet;
        }catch(Exception e){
            System.out.println("加载ReplicaSet失败，在ReplicaSetsServiceImpl类的loadReplicaSetFromYaml方法中");
        }
        return null;


    }

    @Override
    public ReplicaSet createReplicaSetByYaml(String path) throws FileNotFoundException {

        InputStream yamlInputStream = byPath(path);

        try{
            ReplicaSet replicaSet = client.apps().replicaSets().load(yamlInputStream).get();
            String nameSpace = replicaSet.getMetadata().getNamespace();
            System.out.println(nameSpace);
            replicaSet = client.apps().replicaSets().inNamespace(nameSpace).create(replicaSet);
            return replicaSet;
        }catch(Exception e){
            System.out.println("创建ReplicaSet失败，缺少必要的命名空间参数，或是已经有相同的资源对象，在ReplicaSetsServiceImpl类的createReplicaSetByYaml方法");
        }
        return null;

    }

    @Override
    public ReplicaSet createOrReplaceReplicaSet(String path) throws FileNotFoundException {

        InputStream yamlInputStream = byPath(path);

        try {
            ReplicaSet replicaSet = client.apps().replicaSets().load(yamlInputStream).get();
            String nameSpace = replicaSet.getMetadata().getNamespace();

            replicaSet = client.apps().replicaSets().inNamespace(nameSpace).createOrReplace(replicaSet);
            return replicaSet;
        }catch(Exception e){
            System.out.println("创建ReplicaSet失败，缺少必要的命名空间参数，或是已经有相同的资源对象，在ReplicaSetsServiceImpl类的createOrReplaceReplicaSet方法");
        }
        return null;
    }

    @Override
    public void setReplicas(String name, String namespace, Integer replicas){
        try {
            client.apps().replicaSets().inNamespace(namespace).withName(name).edit().getSpec().setReplicas(replicas);
        }catch (Exception e){
            System.out.println("设置ReplicaSet的Replicas失败，在ReplicaSetsServiceImpl类的setReplicas方法中");
        }
    }

    @Override
    public String getReplicaSetYamlByNameAndNamespace(String name ,String namespace){
        try{
            ReplicaSet item = client.apps().replicaSets().inNamespace(namespace).withName(name).get();
            return Yaml.dump(item);
        }catch(Exception e){
            System.out.println("获取Yaml失败，在ReplicaSetsServiceImpl类的getReplicaSetYamlByNameAndNamespace方法中");
        }
        return null;
        }

    @Override
    public List<Pod> getPodReplicaSetInvolved(String name, String namespace){
        List<Pod> pods = new ArrayList<>();
        try{
            //获取 ReplicaSet
            ReplicaSet aReplicaSet = client.apps().replicaSets().inNamespace(namespace).withName(name).get();
            Map<String, String> matchLabels = aReplicaSet.getSpec().getSelector().getMatchLabels();
            String uid = aReplicaSet.getMetadata().getUid();
            //获取 Pods
            PodsServiceImpl podsService = new PodsServiceImpl();
            pods = FilterPodsByControllerUid.filterPodsByControllerUid(uid, podsService.findPodsByLabels(matchLabels));
        }catch (Exception e){
            System.out.println("获取Resources失败，未获取到相应 Pod在，ReplicaSetsServiceImpl类的getPodReplicaSetInvolved方法中");
        }
        return pods;
    }
}

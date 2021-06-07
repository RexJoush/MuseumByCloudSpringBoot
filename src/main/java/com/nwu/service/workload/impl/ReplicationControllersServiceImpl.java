package com.nwu.service.workload.impl;

import com.nwu.service.workload.ReplicationControllersService;
import com.nwu.util.FilterPodsByControllerUid;
import com.nwu.util.KubernetesUtils;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.ReplicationController;
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
 * Replication Controllers 的 service 层实现类
 */
@Service
public class ReplicationControllersServiceImpl implements ReplicationControllersService {

    @Override
    public List<ReplicationController> findAllReplicationControllers(){
        try{
            List<ReplicationController> items = KubernetesUtils.client.replicationControllers().inAnyNamespace().list().getItems();
            return items;
        }catch(Exception e){
            System.out.println("获取ReplicationControllers失败，在ReplicationControllersServiceImpl类的findAllReplicationControllers方法中");
        }
        return null;
    }

    @Override
    public List<ReplicationController> findReplicationControllersByNamespace(String namespace) {
        try{
            List<ReplicationController> items = KubernetesUtils.client.replicationControllers().inNamespace(namespace).list().getItems();

            return items;
        }catch(Exception e){
            System.out.println("获取ReplicationControllers失败，在ReplicationControllersServiceImpl类的findReplicationControllersByNamespace方法中");
        }
        return null;

    }

    @Override
    public ReplicationController getReplicationControllerByNameAndNamespace(String name, String namespace){
        try{
            ReplicationController item = KubernetesUtils.client.replicationControllers().inNamespace(namespace).withName(name).get();
            return item;
        }catch(Exception e){
            System.out.println("获取ReplicationController失败，在ReplicationControllersServiceImpl类的getReplicationControllerByNameAndNamespace方法中");
        }
        return null;

    }

    @Override
    public Boolean deleteReplicationControllerByNameAndNamespace(String name, String namespace){
        try{
            Boolean delete = KubernetesUtils.client.replicationControllers().inNamespace(namespace).withName(name).delete();

            return delete;
        }catch(Exception e){
            System.out.println("删除ReplicationController失败，在ReplicationControllersServiceImpl类的deleteReplicationControllerByNameAndNamespace方法中");
        }
        return null;

    }

    @Override
    public ReplicationController loadReplicationControllerFromYaml(String path) throws FileNotFoundException {

        InputStream yamlInputStream = byPath(path);
        try{
            ReplicationController replicationController = KubernetesUtils.client.replicationControllers().load(yamlInputStream).get();

            return replicationController;
        }catch(Exception e){
            System.out.println("加载ReplicationController失败，在ReplicationControllersServiceImpl类的loadReplicationControllerFromYaml方法中");
        }
        return null;

    }

    @Override
    public ReplicationController createReplicationControllerByYaml(String path) throws FileNotFoundException {

        InputStream yamlInputStream = byPath(path);
        try {
            ReplicationController replicationController = KubernetesUtils.client.replicationControllers().load(yamlInputStream).get();
            String nameSpace = replicationController.getMetadata().getNamespace();
            replicationController = KubernetesUtils.client.replicationControllers().inNamespace(nameSpace).create(replicationController);
            return replicationController;
        }catch(Exception e){
            System.out.println("创建ReplicationController失败，缺少必要的命名空间参数，或是已经有相同的资源对象，在ReplicationControllersServiceImpl类的createReplicationControllerByYaml方法");
        }
        return null;
    }

    @Override
    public ReplicationController createOrReplaceReplicationController(String path) throws FileNotFoundException {
        InputStream yamlInputStream = byPath(path);
        try {
            ReplicationController replicationController = KubernetesUtils.client.replicationControllers().load(yamlInputStream).get();
            String nameSpace = replicationController.getMetadata().getNamespace();
            replicationController = KubernetesUtils.client.replicationControllers().inNamespace(nameSpace).createOrReplace(replicationController);
            return replicationController;
        }catch(Exception e){
            System.out.println("创建ReplicationController失败，缺少必要的命名空间参数，或是已经有相同的资源对象，在ReplicationControllersServiceImpl类的createOrReplaceReplicationController方法");
        }
        return null;
       }


    @Override
    public void setReplicas(String name, String namespace, Integer replicas){
        try {
            KubernetesUtils.client.replicationControllers().inNamespace(namespace).withName(name).edit().getSpec().setReplicas(replicas);
        }catch (Exception e){
            System.out.println("设置ReplicationController的Replicas失败，在ReplicationControllersServiceImpl类的setReplicas方法中");
        }
    }

    @Override
    public String getReplicationControllerYamlByNameAndNamespace(String name ,String namespace){
        try{
            ReplicationController item = KubernetesUtils.client.replicationControllers().inNamespace(namespace).withName(name).get();
            return Yaml.dump(item);
        }catch(Exception e){
            System.out.println("获取Yaml失败，在ReplicationControllersServiceImpl类的getReplicationControllerYamlByNameAndNamespace方法中");
        }
        return null;

    }

    @Override
    public List<Pod> getPodReplicationControllerInvolved(String name, String namespace){
        List<Pod> pods = new ArrayList<>();
        try{
            //获取 ReplicationController
            ReplicationController aReplicationController = KubernetesUtils.client.replicationControllers().inNamespace(namespace).withName(name).get();
            Map<String, String> matchLabels = aReplicationController.getSpec().getSelector();
            String uid = aReplicationController.getMetadata().getUid();
            //获取 Pods
            PodsServiceImpl podsService = new PodsServiceImpl();
            pods = FilterPodsByControllerUid.filterPodsByControllerUid(uid, podsService.findPodsByLabels(matchLabels));
        }catch (Exception e){
            System.out.println("获取Resources失败，未获取到相应 Pod，在ReplicationControllersServiceImpl类的getPodReplicationControllerInvolved方法中");
        }
        return pods;
    }
}

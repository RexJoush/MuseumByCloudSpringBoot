package com.nwu.service.workload.impl;

import com.nwu.service.workload.ReplicaSetsService;
import com.nwu.util.KubernetesConfig;
import io.fabric8.kubernetes.api.model.apps.ReplicaSet;
import io.fabric8.kubernetes.api.model.batch.CronJob;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;

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

        List<ReplicaSet> items = KubernetesConfig.client.apps().replicaSets().inAnyNamespace().list().getItems();

        return items;

    }

    @Override
    public List<ReplicaSet> findReplicaSetsByNamespace(String namespace) {

        List<ReplicaSet> items = KubernetesConfig.client.apps().replicaSets().inNamespace(namespace).list().getItems();

        return items;
    }

    @Override
    public Boolean deleteReplicaSetByNameAndNamespace(String name, String namespace){

        Boolean delete = KubernetesConfig.client.apps().replicaSets().inNamespace(namespace).withName(name).delete();

        return delete;
    }

    @Override
    public ReplicaSet loadReplicaSetFromYaml(InputStream yamlInputStream){

        ReplicaSet replicaSet = KubernetesConfig.client.apps().replicaSets().load(yamlInputStream).get();

        return replicaSet;
    }

    @Override
    public ReplicaSet createReplicaSetByYaml(InputStream yamlInputStream){

        ReplicaSet replicaSet = KubernetesConfig.client.apps().replicaSets().load(yamlInputStream).get();
        String nameSpace = replicaSet.getMetadata().getNamespace();
        System.out.println(nameSpace);
        try{
            replicaSet = KubernetesConfig.client.apps().replicaSets().inNamespace(nameSpace).create(replicaSet);
        }catch(Exception e){
            System.out.println("缺少必要的命名空间参数，或是已经有相同的资源对象，在ReplicaSetsServiceImpl类的createReplicaSetByYaml方法");
        }

        return replicaSet;
    }

    @Override
    public ReplicaSet createOrReplaceReplicaSet(InputStream yamlInputStream){

        ReplicaSet replicaSet = KubernetesConfig.client.apps().replicaSets().load(yamlInputStream).get();
        String nameSpace = replicaSet.getMetadata().getNamespace();

        try {
            replicaSet = KubernetesConfig.client.apps().replicaSets().inNamespace(nameSpace).createOrReplace(replicaSet);
        }catch(Exception e){
            System.out.println("缺少必要的命名空间参数，或是已经有相同的资源对象，在ReplicaSetsServiceImpl类的createOrReplaceReplicaSet方法");
        }
        return replicaSet;
    }

}

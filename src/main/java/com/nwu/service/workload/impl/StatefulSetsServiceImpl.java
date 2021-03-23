package com.nwu.service.workload.impl;

import com.nwu.service.workload.StatefulSetsService;
import com.nwu.util.KubernetesConfig;
import io.fabric8.kubernetes.api.model.apps.StatefulSet;
import io.fabric8.kubernetes.api.model.batch.CronJob;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;

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

        List<StatefulSet> items = KubernetesConfig.client.apps().statefulSets().inAnyNamespace().list().getItems();

        return items;

    }

    @Override
    public List<StatefulSet> findStatefulSetsByNamespace(String namespace) {

        List<StatefulSet> items = KubernetesConfig.client.apps().statefulSets().inNamespace(namespace).list().getItems();

        return items;
    }

    @Override
    public Boolean deleteStatefulSetByNameAndNamespace(String name, String namespace){

        Boolean delete = KubernetesConfig.client.apps().statefulSets().inNamespace(namespace).withName(name).delete();

        return delete;
    }

    @Override
    public StatefulSet loadStatefulSetFromYaml(InputStream yamlInputStream){

        StatefulSet statefulSet = KubernetesConfig.client.apps().statefulSets().load(yamlInputStream).get();

        return statefulSet;
    }

    @Override
    public StatefulSet createStatefulSetByYaml(InputStream yamlInputStream){

        StatefulSet statefulSet = KubernetesConfig.client.apps().statefulSets().load(yamlInputStream).get();
        String nameSpace = statefulSet.getMetadata().getNamespace();
        try {
            statefulSet = KubernetesConfig.client.apps().statefulSets().inNamespace(nameSpace).create(statefulSet);
        }catch(Exception e){
            System.out.println("缺少必要的命名空间参数，或是已经有相同的资源对象，在StatefulSetsServiceImpl类的createStatefulSetByYaml方法");
        }
        return statefulSet;
    }
}

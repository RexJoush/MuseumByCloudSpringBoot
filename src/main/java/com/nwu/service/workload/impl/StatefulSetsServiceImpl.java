package com.nwu.service.workload.impl;

import com.nwu.service.workload.StatefulSetsService;
import com.nwu.util.KubernetesUtils;
import io.fabric8.kubernetes.api.model.apps.StatefulSet;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

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

}

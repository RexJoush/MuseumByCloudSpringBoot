package com.nwu.service.workload.impl;

import com.nwu.service.workload.DaemonSetsService;
import com.nwu.util.KubernetesUtils;
import io.fabric8.kubernetes.api.model.apps.DaemonSet;
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
    public DaemonSet createDaemonSetByYaml(String path) throws FileNotFoundException {

        InputStream yamlInputStream = byPath(path);

        DaemonSet daemonSet = KubernetesUtils.client.apps().daemonSets().load(yamlInputStream).get();
        String nameSpace = daemonSet.getMetadata().getNamespace();
        try {
            daemonSet = KubernetesUtils.client.apps().daemonSets().inNamespace(nameSpace).create(daemonSet);
        }catch(Exception e){
            System.out.println("缺少必要的命名空间参数，或是已经有相同的资源对象，在DaemonSetsServiceImpl类的createDaemonSetByYaml方法");
        }
        return daemonSet;
    }

    @Override
    public DaemonSet createOrReplaceDaemonSet(String path) throws FileNotFoundException {

        InputStream yamlInputStream = byPath(path);

        DaemonSet daemonSet = KubernetesUtils.client.apps().daemonSets().load(yamlInputStream).get();
        String nameSpace = daemonSet.getMetadata().getNamespace();

        try {
            daemonSet = KubernetesUtils.client.apps().daemonSets().inNamespace(nameSpace).createOrReplace(daemonSet);
        }catch(Exception e){
            System.out.println("缺少必要的命名空间参数，或是已经有相同的资源对象，在DaemonSetsServiceImpl类的createOrReplaceDaemonSet方法");
        }
        return daemonSet;
    }

}

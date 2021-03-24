package com.nwu.service.workload.impl;

import com.nwu.service.workload.DaemonSetsService;
import com.nwu.util.KubernetesConfig;
import io.fabric8.kubernetes.api.model.apps.DaemonSet;
import io.fabric8.kubernetes.api.model.batch.CronJob;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import static com.nwu.service.getYamlInputStream.byPath;

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

        List<DaemonSet> items = KubernetesConfig.client.apps().daemonSets().inAnyNamespace().list().getItems();

        return items;

    }

    @Override
    public List<DaemonSet> findDaemonSetsByNamespace(String namespace) {

        List<DaemonSet> items = KubernetesConfig.client.apps().daemonSets().inNamespace(namespace).list().getItems();

        return items;
    }

    @Override
    public Boolean deleteDaemonSetByNameAndNamespace(String name, String namespace){
        Boolean delete = KubernetesConfig.client.apps().daemonSets().inNamespace(namespace).withName(name).delete();
        return delete;
    }

    @Override
    public DaemonSet loadDaemonSetFromYaml(String path) throws FileNotFoundException {

        InputStream yamlInputStream = byPath(path);

        DaemonSet daemonSet = KubernetesConfig.client.apps().daemonSets().load(yamlInputStream).get();
        return daemonSet;
    }

    @Override
    public DaemonSet createDaemonSetByYaml(String path) throws FileNotFoundException {

        InputStream yamlInputStream = byPath(path);

        DaemonSet daemonSet = KubernetesConfig.client.apps().daemonSets().load(yamlInputStream).get();
        String nameSpace = daemonSet.getMetadata().getNamespace();
        try {
            daemonSet = KubernetesConfig.client.apps().daemonSets().inNamespace(nameSpace).create(daemonSet);
        }catch(Exception e){
            System.out.println("缺少必要的命名空间参数，或是已经有相同的资源对象，在DaemonSetsServiceImpl类的createDaemonSetByYaml方法");
        }
        return daemonSet;
    }

    @Override
    public DaemonSet createOrReplaceDaemonSet(String path) throws FileNotFoundException {

        InputStream yamlInputStream = byPath(path);

        DaemonSet daemonSet = KubernetesConfig.client.apps().daemonSets().load(yamlInputStream).get();
        String nameSpace = daemonSet.getMetadata().getNamespace();

        try {
            daemonSet = KubernetesConfig.client.apps().daemonSets().inNamespace(nameSpace).createOrReplace(daemonSet);
        }catch(Exception e){
            System.out.println("缺少必要的命名空间参数，或是已经有相同的资源对象，在DaemonSetsServiceImpl类的createOrReplaceDaemonSet方法");
        }
        return daemonSet;
    }

}

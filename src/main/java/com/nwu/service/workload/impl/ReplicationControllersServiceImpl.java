package com.nwu.service.workload.impl;

import com.nwu.service.workload.ReplicationControllersService;
import com.nwu.util.KubernetesUtils;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.ReplicationController;
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
 * Replication Controllers 的 service 层实现类
 */
@Service
public class ReplicationControllersServiceImpl implements ReplicationControllersService {

    @Override
    public List<ReplicationController> findAllReplicationControllers(){

        List<ReplicationController> items = KubernetesUtils.client.replicationControllers().inAnyNamespace().list().getItems();

        return items;

    }

    @Override
    public List<ReplicationController> findReplicationControllersByNamespace(String namespace) {

        List<ReplicationController> items = KubernetesUtils.client.replicationControllers().inNamespace(namespace).list().getItems();

        return items;
    }

    @Override
    public ReplicationController getReplicationControllerByNameAndNamespace(String name, String namespace){
        ReplicationController items = KubernetesUtils.client.replicationControllers().inNamespace(namespace).withName(name).get();
        return items;
    }

    @Override
    public Boolean deleteReplicationControllerByNameAndNamespace(String name, String namespace){

        Boolean delete = KubernetesUtils.client.replicationControllers().inNamespace(namespace).withName(name).delete();

        return delete;
    }

    @Override
    public ReplicationController loadReplicationControllerFromYaml(String path) throws FileNotFoundException {

        InputStream yamlInputStream = byPath(path);

        ReplicationController replicationController = KubernetesUtils.client.replicationControllers().load(yamlInputStream).get();

        return replicationController;
    }

    @Override
    public ReplicationController createReplicationControllerByYaml(String path) throws FileNotFoundException {

        InputStream yamlInputStream = byPath(path);

        ReplicationController replicationController = KubernetesUtils.client.replicationControllers().load(yamlInputStream).get();
        String nameSpace = replicationController.getMetadata().getNamespace();
        try {
            replicationController = KubernetesUtils.client.replicationControllers().inNamespace(nameSpace).create(replicationController);
        }catch(Exception e){
            System.out.println("缺少必要的命名空间参数，或是已经有相同的资源对象，在ReplicationControllersServiceImpl类的createReplicationControllerByYaml方法");
        }
        return replicationController;
    }

    @Override
    public ReplicationController createOrReplaceReplicationController(String path) throws FileNotFoundException {

        InputStream yamlInputStream = byPath(path);

        ReplicationController replicationController = KubernetesUtils.client.replicationControllers().load(yamlInputStream).get();
        String nameSpace = replicationController.getMetadata().getNamespace();

        try {
            replicationController = KubernetesUtils.client.replicationControllers().inNamespace(nameSpace).createOrReplace(replicationController);
        }catch(Exception e){
            System.out.println("缺少必要的命名空间参数，或是已经有相同的资源对象，在ReplicationControllersServiceImpl类的createOrReplaceReplicationController方法");
        }
        return replicationController;
    }


    @Override
    public void setReplicas(String name, String namespace, Integer replicas){
        try {
            KubernetesUtils.client.replicationControllers().inNamespace(namespace).withName(name).edit().getSpec().setReplicas(replicas);
        }catch (Exception e){
            System.out.println("设置ReplicationController的replicas失败");
        }
    }
}

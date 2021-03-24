package com.nwu.service.workload.impl;

import com.nwu.service.workload.DeploymentsService;
import com.nwu.util.KubernetesConfig;
import io.fabric8.kubernetes.api.model.apps.DaemonSet;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.batch.CronJob;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;

/**
 * @author Rex Joush
 * @time 2021.03.22
 */

/**
 *  Deployments 的 service 层实现类
 */
@Service
public class DeploymentsServiceImpl implements DeploymentsService {
    @Override
    public List<Deployment> findAllDeployments(){

        List<Deployment> items = KubernetesConfig.client.apps().deployments().inAnyNamespace().list().getItems();

        return items;

    }

    @Override
    public List<Deployment> findDeploymentsByNamespace(String namespace) {

        List<Deployment> items = KubernetesConfig.client.apps().deployments().inNamespace(namespace).list().getItems();
        return items;

    }

    @Override
    public Boolean deleteDeploymentByNameAndNamespace(String name, String namespace){

        Boolean delete = KubernetesConfig.client.apps().deployments().inNamespace(namespace).withName(name).delete();

        return delete;
    }

    @Override
    public Deployment loadDeploymentFromYaml(InputStream yamlInputStream){

        Deployment deployment = KubernetesConfig.client.apps().deployments().load(yamlInputStream).get();

        return deployment;
    }

    @Override
    public Deployment createDeploymentByYaml(InputStream yamlInputStream){

        Deployment deployment = KubernetesConfig.client.apps().deployments().load(yamlInputStream).get();
        String nameSpace = deployment.getMetadata().getNamespace();
        try {
            deployment = KubernetesConfig.client.apps().deployments().inNamespace(nameSpace).create(deployment);
        }catch(Exception e){
            System.out.println("缺少必要的命名空间参数，或是已经有相同的资源对象，在DeploymentsServiceImpl类的createDeploymentByYaml方法");
        }
        return deployment;
    }

    @Override
    public Deployment createOrReplaceDeployment(InputStream yamlInputStream){

        Deployment deployment = KubernetesConfig.client.apps().deployments().load(yamlInputStream).get();
        String nameSpace = deployment.getMetadata().getNamespace();

        try {
            deployment = KubernetesConfig.client.apps().deployments().inNamespace(nameSpace).createOrReplace(deployment);
        }catch(Exception e){
            System.out.println("缺少必要的命名空间参数，或是已经有相同的资源对象，在DeploymentsServiceImpl类的createOrReplaceDeployment方法");
        }
        return deployment;
    }


    @Override
    public String getDeploymentLogByNameAndNamespace(String name, String namespace){
        String log = "";
        try{
            log = KubernetesConfig.client.apps().deployments().inNamespace(namespace).withName(name).getLog();
        }catch(Exception e){
            System.out.println("未获取到Deployment的日志");
        }

        return log;
    }
}

package com.nwu.service.workload.impl;

import com.nwu.service.workload.DeploymentsService;
import com.nwu.util.KubernetesUtils;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.batch.CronJob;
import io.kubernetes.client.util.Yaml;
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
 *  Deployments 的 service 层实现类
 */
@Service
public class DeploymentsServiceImpl implements DeploymentsService {
    @Override
    public List<Deployment> findAllDeployments(){

        List<Deployment> items = KubernetesUtils.client.apps().deployments().inAnyNamespace().list().getItems();

        return items;

    }

    @Override
    public List<Deployment> findDeploymentsByNamespace(String namespace) {

        return KubernetesUtils.client.apps().deployments().inNamespace(namespace).list().getItems();

    }

    @Override
    public Deployment getDeploymentByNameAndNamespace(String name, String namespace) {
        return KubernetesUtils.client.apps().deployments().inNamespace(namespace).withName(name).get();
    }

    @Override
    public Boolean deleteDeploymentByNameAndNamespace(String name, String namespace){

        Boolean delete = KubernetesUtils.client.apps().deployments().inNamespace(namespace).withName(name).delete();

        return delete;
    }

    @Override
    public Deployment loadDeploymentFromYaml(String path) throws FileNotFoundException {

        InputStream yamlInputStream = byPath(path);


        Deployment deployment = KubernetesUtils.client.apps().deployments().load(yamlInputStream).get();

        return deployment;
    }

    @Override
    public Deployment createDeploymentByYaml(String path) throws FileNotFoundException {

        InputStream yamlInputStream = byPath(path);

        Deployment deployment = KubernetesUtils.client.apps().deployments().load(yamlInputStream).get();
        String nameSpace = deployment.getMetadata().getNamespace();
        try {
            deployment = KubernetesUtils.client.apps().deployments().inNamespace(nameSpace).create(deployment);
        }catch(Exception e){
            System.out.println("缺少必要的命名空间参数，或是已经有相同的资源对象，在DeploymentsServiceImpl类的createDeploymentByYaml方法");
        }
        return deployment;
    }

    @Override
    public Deployment createOrReplaceDeployment(String path) throws FileNotFoundException {

        InputStream yamlInputStream = byPath(path);

        Deployment deployment = KubernetesUtils.client.apps().deployments().load(yamlInputStream).get();
        String nameSpace = deployment.getMetadata().getNamespace();

        try {
            deployment = KubernetesUtils.client.apps().deployments().inNamespace(nameSpace).createOrReplace(deployment);
        }catch(Exception e){
            System.out.println("缺少必要的命名空间参数，或是已经有相同的资源对象，在DeploymentsServiceImpl类的createOrReplaceDeployment方法");
        }
        return deployment;
    }


    @Override
    public String getDeploymentLogByNameAndNamespace(String name, String namespace){
        String log = "";
        try{
            log = KubernetesUtils.client.apps().deployments().inNamespace(namespace).withName(name).getLog();
        }catch(Exception e){
            System.out.println("未获取到Deployment的日志");
        }

        return log;
    }

    @Override
    public void setReplicas(String name, String namespace, Integer replicas){
        System.out.println(replicas);
        try{
            Deployment deployment = KubernetesUtils.client.apps().deployments().inNamespace(namespace)
                    .withName(name).get();
            deployment.getSpec().setReplicas(replicas);
            KubernetesUtils.client.apps().deployments().inNamespace(namespace).withName(name).delete();
            KubernetesUtils.client.apps().deployments().createOrReplace(deployment);
//            KubernetesUtils.client.apps().deployments().inNamespace(namespace)
//                    .withName(name).edit().getSpec().setReplicas(replicas);
        }catch(Exception e){
            System.out.println("设置Deployment的replicas失败");
        }
    }

    @Override
    public String getDeploymentYamlByNameAndNamespace(String name ,String namespace){
        Deployment item = KubernetesUtils.client.apps().deployments().inNamespace(namespace).withName(name).get();
        return Yaml.dump(item);
    }
}

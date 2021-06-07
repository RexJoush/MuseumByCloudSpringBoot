package com.nwu.service.workload.impl;

import com.nwu.service.workload.DeploymentsService;
import com.nwu.util.KubernetesUtils;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.kubernetes.client.custom.V1Patch;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.apis.AppsV1Api;
import io.kubernetes.client.openapi.models.V1Deployment;
import io.kubernetes.client.util.Yaml;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
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
        try{
            List<Deployment> items = KubernetesUtils.client.apps().deployments().inAnyNamespace().list().getItems();
            return items;
        }catch(Exception e){
            System.out.println("获取Deployments失败，在DeploymentsServiceImpl类的findAllDeployments方法中");
        }
        return null;
    }

    @Override
    public List<Deployment> findDeploymentsByNamespace(String namespace) {
        try{
            return KubernetesUtils.client.apps().deployments().inNamespace(namespace).list().getItems();
        }catch (Exception e){
            System.out.println("获取Deployments失败，在DeploymentsServiceImpl类的findDeploymentsByNamespace方法中");
        }
        return null;
    }

    @Override
    public Deployment getDeploymentByNameAndNamespace(String name, String namespace) {
        try{
            return KubernetesUtils.client.apps().deployments().inNamespace(namespace).withName(name).get();
        }catch(Exception e){
            System.out.println("获取Deployment失败，在DeploymentsServiceImpl类的getDeploymentByNameAndNamespace方法中");
        }
        return null;
    }

    @Override
    public Boolean deleteDeploymentByNameAndNamespace(String name, String namespace){
        try{
            Boolean delete = KubernetesUtils.client.apps().deployments().inNamespace(namespace).withName(name).delete();
            return delete;
        }catch(Exception e){
            System.out.println("删除Deployment失败，在DeploymentsServiceImpl类的deleteDeploymentByNameAndNamespace方法中");
        }
        return null;
    }

    @Override
    public Deployment loadDeploymentFromYaml(String path) throws FileNotFoundException {
        InputStream yamlInputStream = byPath(path);
        try{
            Deployment deployment = KubernetesUtils.client.apps().deployments().load(yamlInputStream).get();
            return deployment;
        }catch (Exception e){
            System.out.println("加载Deployment失败，在DeploymentsServiceImpl类的loadDeploymentFromYaml方法中");
        }
        return null;
    }

    @Override
    public Deployment createOrReplaceDeploymentByPath(String path) throws FileNotFoundException {
        InputStream yamlInputStream = byPath(path);
        try {
            Deployment deployment = KubernetesUtils.client.apps().deployments().load(yamlInputStream).get();
            String nameSpace = deployment.getMetadata().getNamespace();
            deployment = KubernetesUtils.client.apps().deployments().inNamespace(nameSpace).create(deployment);
            return deployment;
        }catch(Exception e){
            System.out.println("创建Deployment失败，缺少必要的命名空间参数，或是已经有相同的资源对象，在DeploymentsServiceImpl类的createDeploymentByYaml方法");
        }
        return null;
    }

    @Override
    public Deployment createOrReplaceDeploymentByFile(File file) throws FileNotFoundException, ApiException {

        InputStream yamlInputStream = new FileInputStream(file);


        try {

//        String nameSpace = deployment.getMetadata().getNamespace();
            Deployment deployment = KubernetesUtils.client.apps().deployments().load(yamlInputStream).get();
            String name = deployment.getMetadata().getName();
            String namespace = deployment.getMetadata().getNamespace();

//        Deployment deployment = new Deployment();

            AppsV1Api appsV1Api = new AppsV1Api();
            V1Deployment v1Deployment = appsV1Api.readNamespacedDeployment(name, namespace, "", false, false);
//        appsV1Api.replaceNamespacedDeployment(name, namespace, new V1Deployment() = deployment)
//        KubernetesUtils.client.apps().deployments().load(yamlInputStream).replace(deployment);
            deployment = KubernetesUtils.client.apps().deployments().load(yamlInputStream).createOrReplace();

            return deployment;
        }catch(Exception e){
            System.out.println(e);
            System.out.println("创建Deployment失败，缺少必要的命名空间参数，或是已经有相同的资源对象，在DeploymentsServiceImpl类的createOrReplaceDeployment方法");
        }
        return null;
    }


//    @Override //弃用，不获取Log，获取Event，因为在这获取的是container的Log
//    public String getDeploymentLogByNameAndNamespace(String name, String namespace){
//        String log = "";
//        try{
//            log = KubernetesUtils.client.apps().deployments().inNamespace(namespace).withName(name).getLog();
//        }catch(Exception e){
//            System.out.println("未获取到Deployment的日志");
//        }
//        return log;
//    }

    @Override
    public Boolean setReplicas(String name, String namespace, Integer replicas){

        /**
         * 方法一
         */
        try {
            AppsV1Api apiInstance = new AppsV1Api();
            // 更新副本的json串
            String jsonPatchStr = "[{\"op\":\"replace\",\"path\":\"/spec/replicas\", \"value\": " + replicas + " }]";
            V1Patch body = new V1Patch(jsonPatchStr);
            apiInstance.patchNamespacedDeployment(name, namespace, body, null, null, null, null);
            return true;
        } catch (ApiException e) {
            e.printStackTrace();
            System.out.println("更新Replicas失败，在DeploymentsServiceImpl的setReplicas方法中");
        }

        /**
         * 方法二
         */
//        try{
//            KubernetesUtils.client.apps().deployments().inNamespace(namespace).withName(name).scale(replicas);
//            return true;
//        }catch(Exception e){
//            System.out.println("设置Deployment的replicas失败");
//            return false;
//
//        }
        return null;
    }

    @Override
    public String getDeploymentYamlByNameAndNamespace(String name ,String namespace) {
        try {
            V1Deployment v1Deployment = KubernetesUtils.appsV1Api.readNamespacedDeployment(name,namespace, null, null, null);
            return Yaml.dump(v1Deployment);
        } catch (ApiException e) {
            e.printStackTrace();
            System.out.println("获取Yaml失败，在DeploymentsServiceImpl类的getDeploymentYamlByNameAndNamespace方法中");
        }
        return null;
    }
}

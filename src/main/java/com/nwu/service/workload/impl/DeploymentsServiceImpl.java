package com.nwu.service.workload.impl;

import com.nwu.entity.workload.ReplicaSet.ReplicaSetInformation;
import com.nwu.service.impl.CommonServiceImpl;
import com.nwu.service.workload.DeploymentsService;
import com.nwu.util.FilterReplicaSetByControllerUid;
import com.nwu.util.KubernetesUtils;
import com.nwu.util.format.ReplicaSetFormat;
import io.fabric8.kubernetes.api.model.Event;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.ReplicaSet;
import io.kubernetes.client.custom.V1Patch;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.apis.AppsV1Api;
import io.kubernetes.client.openapi.models.V1Deployment;
import io.kubernetes.client.util.Yaml;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public Pair<Integer, List<Deployment>> findAllDeployments(){
        try{
            List<Deployment> items = KubernetesUtils.client.apps().deployments().inAnyNamespace().list().getItems();
            return Pair.of(1200, items);
        }catch(Exception e){
            System.out.println("获取Deployments失败，在DeploymentsServiceImpl类的findAllDeployments方法中");
        }
        return Pair.of(1201, null);
    }

    @Override
    public Pair<Integer, List<Deployment>> findDeploymentsByNamespace(String namespace) {
        try{
            List<Deployment> items = KubernetesUtils.client.apps().deployments().inNamespace(namespace).list().getItems();
            return Pair.of(1200, items);
        }catch (Exception e){
            System.out.println("获取Deployments失败，在DeploymentsServiceImpl类的findDeploymentsByNamespace方法中");
        }
        return Pair.of(1201, null);
    }

    @Override
    public Pair<Integer, Deployment> getDeploymentByNameAndNamespace(String name, String namespace) {
        try{
            Deployment deployment = KubernetesUtils.client.apps().deployments().inNamespace(namespace).withName(name).get();
            return Pair.of(1200, deployment);
        }catch(Exception e){
            System.out.println("获取Deployment失败，在DeploymentsServiceImpl类的getDeploymentByNameAndNamespace方法中");
        }
        return Pair.of(1201, null);
    }

    @Override
    public Pair<Integer, Boolean> deleteDeploymentByNameAndNamespace(String name, String namespace){
        try{
            Boolean delete = KubernetesUtils.client.apps().deployments().inNamespace(namespace).withName(name).delete();
            return Pair.of(1200, delete);
        }catch(Exception e){
            System.out.println("删除Deployment失败，在DeploymentsServiceImpl类的deleteDeploymentByNameAndNamespace方法中");
        }
        return Pair.of(1201, null);
    }

    @Override
    public Pair<Integer, Deployment> loadDeploymentFromYaml(String path) throws FileNotFoundException {
        InputStream yamlInputStream = byPath(path);
        try{
            Deployment deployment = KubernetesUtils.client.apps().deployments().load(yamlInputStream).get();
            return Pair.of(1200, deployment);
        }catch (Exception e){
            System.out.println("加载Deployment失败，在DeploymentsServiceImpl类的loadDeploymentFromYaml方法中");
        }
        return Pair.of(1201, null);
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
    public Pair<Integer, Boolean> createOrReplaceDeploymentByYamlString(String yaml){
        try{
            Deployment deployment = Yaml.loadAs(yaml, Deployment.class);
            KubernetesUtils.client.apps().deployments().inNamespace(deployment.getMetadata().getNamespace()).withName(deployment.getMetadata().getName()).createOrReplace(deployment);
            return Pair.of(1200, true);
        }catch (Exception e){
            System.out.println("创建Deployment失败，请检查 Yaml 格式或是否重名，在DeploymentsServiceImpl类的createDeploymentByYaml方法");
        }
        return Pair.of(1201, null);
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
    public Pair<Integer, Boolean> setReplicas(String name, String namespace, Integer replicas){

        /**
         * 方法一
         */
        try {
            AppsV1Api apiInstance = new AppsV1Api();
            // 更新副本的json串
            String jsonPatchStr = "[{\"op\":\"replace\",\"path\":\"/spec/replicas\", \"value\": " + replicas + " }]";
            V1Patch body = new V1Patch(jsonPatchStr);
            apiInstance.patchNamespacedDeployment(name, namespace, body, null, null, null, null);
            return Pair.of(1200, true);
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
        return Pair.of(1201, null);
    }

    @Override
    public Pair<Integer, String> getDeploymentYamlByNameAndNamespace(String name ,String namespace) {
        try {
            Deployment deployment = KubernetesUtils.client.apps().deployments().inNamespace(namespace).withName(name).get();
            return Pair.of(1200, Yaml.dump(deployment));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("获取Yaml失败，在DeploymentsServiceImpl类的getDeploymentYamlByNameAndNamespace方法中");
        }
        return Pair.of(1201, null);
    }

    @Override
    public Pair<Integer, Map> getDeploymentResources(String name, String namespace){
        try{
            Pair<Integer, Deployment> pair = this.getDeploymentByNameAndNamespace(name, namespace);

            if(pair.getLeft() != 1200) return Pair.of(1201, null);// 操作失败
            else if(pair.getLeft() == 1200 && pair.getRight() == null) return Pair.of(1202, null);// 非法操作
            Deployment deployment = pair.getRight();

            //获取 Deployment 下的 ReplicaSet(新[0]旧[1:-1]) 正确匹配按ControllerUid
            ReplicaSetsServiceImpl replicaSetsService = new ReplicaSetsServiceImpl();
            List<ReplicaSet> replicaSetList = KubernetesUtils.client.apps().replicaSets().inAnyNamespace().list().getItems();
            replicaSetList = FilterReplicaSetByControllerUid.filterReplicaSetsByControllerUid(deployment.getMetadata().getUid(), replicaSetList);
            List<ReplicaSetInformation> replicaSets = ReplicaSetFormat.formatReplicaSetList(replicaSetList);

            //获取 Deployment 下的 ReplicaSet(新[0]旧[1:-1]) 错误匹配按Selector
//        Map<String, String> m = deployment.getSpec().getSelector().getMatchLabels();
//        ReplicaSetsServiceImpl replicaSetsService = new ReplicaSetsServiceImpl();
//        List<ReplicaSet> replicaSetList = KubernetesUtils.client.apps().replicaSets().inAnyNamespace().withLabels(m).list().getItems();
//        List<ReplicaSetInformation> replicaSets = ReplicaSetFormat.formatReplicaSetList(replicaSetList);

            int flag= 0;
            String minimum = replicaSets.get(0).getCreationTimestamp().replace("T", "").replace("Z", "");
            for(int i = 1; i < replicaSets.size(); i ++){
                String tmp = replicaSets.get(i).getCreationTimestamp().replace("T", "").replace("Z", "");
                if(tmp.compareTo(minimum) == -1){
                    minimum = tmp;
                    flag = i;
                }
            }
            ReplicaSetInformation tmpReplicaSetInformation = new ReplicaSetInformation();
            tmpReplicaSetInformation = replicaSets.get(0);
            replicaSets.set(0, replicaSets.get(flag));
            replicaSets.set(flag, tmpReplicaSetInformation);

            //获取事件
            Pair<Integer, List<Event>> pairOfEvents = CommonServiceImpl.getEventByInvolvedObjectUid(deployment.getMetadata().getUid());

            //封装数据
            Map<String, Object> data = new HashMap<>();
            flag = 0;//标记哪个数据没获取到
            data.put("deployment", deployment);
            if(replicaSets != null) {
                data.put("newReplicaSets", replicaSets.subList(0,1));
                data.put("oldReplicaSets", replicaSets.subList(1, replicaSets.size()));
            }else {
                data.put("newReplicaSets", null);
                data.put("oldReplicaSets", null);
                flag |= (1 << 2) | (1 << 1);
            }
            if(pairOfEvents.getRight() != null) {
                data.put("events", pairOfEvents.getRight());
            }else{
                data.put("events",null);
                flag |= 1;
            }
            data.put("flag", flag);

            if(flag > 0) return Pair.of(1203, data);
            return Pair.of(1200, data);
        }catch (Exception e){
            System.out.println("请求失败，在 DeploymentsServiceImpl 类的 getDeploymentResources 方法中");
        }
        return Pair.of(1201, null);
    }

}

package com.nwu.service.workload.impl;

import com.nwu.service.explorebalancing.impl.ServicesServiceImpl;
import com.nwu.service.impl.CommonServiceImpl;
import com.nwu.service.workload.ReplicaSetsService;
import com.nwu.util.FilterPodsByControllerUid;
import com.nwu.util.KubernetesUtils;
import com.nwu.util.format.PodFormat;
import io.fabric8.kubernetes.api.model.Event;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.apps.ReplicaSet;
import io.kubernetes.client.util.Yaml;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.nwu.util.GetYamlInputStream.byPath;
import static com.nwu.util.KubernetesUtils.client;

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
    public Pair<Integer, List<ReplicaSet>> findAllReplicaSets(){
        try{
            List<ReplicaSet> items = client.apps().replicaSets().inAnyNamespace().list().getItems();

            return Pair.of(1200, items);
        }catch(Exception e){
            System.out.println("获取ReplicaSets失败，在ReplicaSetsServiceImpl类的findAllReplicaSets方法中");
        }
        return Pair.of(1201, null);


    }

    @Override
    public Pair<Integer, List<ReplicaSet>> findReplicaSetsByNamespace(String namespace) {
        try{
            List<ReplicaSet> items = client.apps().replicaSets().inNamespace(namespace).list().getItems();

            return Pair.of(1200, items);
        }catch(Exception e){
            System.out.println("获取ReplicaSets失败，在ReplicaSetsServiceImpl类的findReplicaSetsByNamespace方法中");
        }
        return Pair.of(1201, null);

    }

    @Override
    public Pair<Integer, ReplicaSet> getReplicaSetByNameAndNamespace(String name, String namespace){
        try{
            ReplicaSet items = client.apps().replicaSets().inNamespace(namespace).withName(name).get();
            return Pair.of(1200, items);
        }catch(Exception e){
            System.out.println("获取ReplicaSet失败，在ReplicaSetsServiceImpl类的getReplicaSetByNameAndNamespace方法中");
        }
        return Pair.of(1201, null);

    }

    @Override
    public Pair<Integer, Boolean> deleteReplicaSetByNameAndNamespace(String name, String namespace){
        try{
            Boolean delete = client.apps().replicaSets().inNamespace(namespace).withName(name).delete();
            return Pair.of(1200, delete);
        }catch(Exception e){
            System.out.println("删除ReplicaSet失败，在ReplicaSetsServiceImpl类的deleteReplicaSetByNameAndNamespace方法中");
        }
        return Pair.of(1201, null);

    }

    @Override
    public Pair<Integer, ReplicaSet> loadReplicaSetFromYaml(String path) throws FileNotFoundException {
        InputStream yamlInputStream = byPath(path);
        try{
            ReplicaSet replicaSet = client.apps().replicaSets().load(yamlInputStream).get();

            return Pair.of(1200, replicaSet);
        }catch(Exception e){
            System.out.println("加载ReplicaSet失败，在ReplicaSetsServiceImpl类的loadReplicaSetFromYaml方法中");
        }
        return Pair.of(1201, null);


    }

    @Override
    public Pair<Integer, Boolean> createOrReplaceReplicaSetByYamlString(String yaml){
        try{
            ReplicaSet replicaSet = Yaml.loadAs(yaml, ReplicaSet.class);
            KubernetesUtils.client.apps().replicaSets().inNamespace(replicaSet.getMetadata().getNamespace()).withName(replicaSet.getMetadata().getName()).createOrReplace(replicaSet);
            return Pair.of(1200, true);
        }catch (Exception e){
            System.out.println("创建 ReplicaSet 失败，请检查 Yaml 格式或是否重名，在 ReplicaSetsServiceImpl 类的 createOrReplaceReplicaSetByYamlString 方法中");
        }
        return Pair.of(1201, null);
    }

    @Override
    public ReplicaSet createReplicaSetByYaml(String path) throws FileNotFoundException {

        InputStream yamlInputStream = byPath(path);

        try{
            ReplicaSet replicaSet = client.apps().replicaSets().load(yamlInputStream).get();
            String nameSpace = replicaSet.getMetadata().getNamespace();
            System.out.println(nameSpace);
            replicaSet = client.apps().replicaSets().inNamespace(nameSpace).create(replicaSet);
            return replicaSet;
        }catch(Exception e){
            System.out.println("创建ReplicaSet失败，缺少必要的命名空间参数，或是已经有相同的资源对象，在ReplicaSetsServiceImpl类的createReplicaSetByYaml方法");
        }
        return null;

    }

    @Override
    public ReplicaSet createOrReplaceReplicaSet(String path) throws FileNotFoundException {

        InputStream yamlInputStream = byPath(path);

        try {
            ReplicaSet replicaSet = client.apps().replicaSets().load(yamlInputStream).get();
            String nameSpace = replicaSet.getMetadata().getNamespace();

            replicaSet = client.apps().replicaSets().inNamespace(nameSpace).createOrReplace(replicaSet);
            return replicaSet;
        }catch(Exception e){
            System.out.println("创建ReplicaSet失败，缺少必要的命名空间参数，或是已经有相同的资源对象，在ReplicaSetsServiceImpl类的createOrReplaceReplicaSet方法");
        }
        return null;
    }

    @Override
    public Pair<Integer, Boolean> setReplicas(String name, String namespace, Integer replicas){
        try {
            client.apps().replicaSets().inNamespace(namespace).withName(name).edit().getSpec().setReplicas(replicas);
            return Pair.of(1200, true);
        }catch (Exception e){
            System.out.println("设置ReplicaSet的Replicas失败，在ReplicaSetsServiceImpl类的setReplicas方法中");
        }
        return Pair.of(1201, null);
    }

    @Override
    public Pair<Integer, String> getReplicaSetYamlByNameAndNamespace(String name ,String namespace){
        try{
            ReplicaSet item = client.apps().replicaSets().inNamespace(namespace).withName(name).get();
            return Pair.of(1200, Yaml.dump(item));
        }catch(Exception e){
            System.out.println("获取Yaml失败，在ReplicaSetsServiceImpl类的getReplicaSetYamlByNameAndNamespace方法中");
        }
        return Pair.of(1201, null);
        }

    @Override
    public Pair<Integer, List<Pod>> getPodReplicaSetInvolved(String name, String namespace){
        List<Pod> pods = new ArrayList<>();
        try{
            //获取 ReplicaSet
            ReplicaSet aReplicaSet = client.apps().replicaSets().inNamespace(namespace).withName(name).get();
            Map<String, String> matchLabels = aReplicaSet.getSpec().getSelector().getMatchLabels();
            String uid = aReplicaSet.getMetadata().getUid();
            //获取 Pods
            PodsServiceImpl podsService = new PodsServiceImpl();
            pods = FilterPodsByControllerUid.filterPodsByControllerUid(uid, podsService.findPodsByLabels(matchLabels));
            return Pair.of(1200, pods);
        }catch (Exception e){
            System.out.println("获取Resources失败，未获取到相应 Pod在，ReplicaSetsServiceImpl类的getPodReplicaSetInvolved方法中");
        }
        return Pair.of(1201, null);
    }

    @Override
    public Pair<Integer, Map> getReplicaSetResources(String name, String namespace){
        try{
            //获取 ReplicaSet
            Pair<Integer, ReplicaSet> pair = this.getReplicaSetByNameAndNamespace(name, namespace);

            if(pair.getLeft() != 1200) return Pair.of(1201, null);
            else if(pair.getLeft() == 1200 && pair.getRight() == null) return Pair.of(1202, null);
            ReplicaSet replicaSet = pair.getRight();

            String uid = replicaSet.getMetadata().getUid();
            Map<String, String> matchLabels = replicaSet.getSpec().getSelector().getMatchLabels();

            //获取 Pods
            PodsServiceImpl podsService = new PodsServiceImpl();
            List<Pod> pods = FilterPodsByControllerUid.filterPodsByControllerUid(uid, podsService.findPodsByLabels(matchLabels));

            //获取 Services
            ServicesServiceImpl servicesService = new ServicesServiceImpl();
            List<io.fabric8.kubernetes.api.model.Service> services = servicesService.getServicesByLabels(matchLabels);

            //获取事件
            Pair<Integer, List<Event>> pairOfEvents = CommonServiceImpl.getEventByInvolvedObjectUid(replicaSet.getMetadata().getUid());

            //封装数据
            Map<String, Object> data = new HashMap<>();
            int flag = 0;//标记哪个数据没获取到
            data.put("replicaSet", replicaSet);
            if(pods != null) {
                data.put("pods", PodFormat.formatPodList(pods));
            }else {
                data.put("pods", null);
                flag |= (1 << 2);
            }
            if(services != null) {
                data.put("services", services);
            }else{
                data.put("services", null);
                flag |= (1 << 1);
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
            System.out.println("请求失败，在 ReplicaSetsServiceImpl 类的 getReplicaSetResources 方法中");
        }
        return Pair.of(1201, null);
    }

}

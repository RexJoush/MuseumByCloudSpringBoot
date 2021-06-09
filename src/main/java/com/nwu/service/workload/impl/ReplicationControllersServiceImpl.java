package com.nwu.service.workload.impl;

import com.nwu.service.explorebalancing.impl.ServicesServiceImpl;
import com.nwu.service.impl.CommonServiceImpl;
import com.nwu.service.workload.ReplicationControllersService;
import com.nwu.util.FilterPodsByControllerUid;
import com.nwu.util.KubernetesUtils;
import com.nwu.util.format.PodFormat;
import io.fabric8.kubernetes.api.model.Event;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.ReplicationController;
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
    public Pair<Integer, List<ReplicationController>> findAllReplicationControllers(){
        try{
            List<ReplicationController> items = KubernetesUtils.client.replicationControllers().inAnyNamespace().list().getItems();
            return Pair.of(1200, items);
        }catch(Exception e){
            System.out.println("获取ReplicationControllers失败，在ReplicationControllersServiceImpl类的findAllReplicationControllers方法中");
        }
        return Pair.of(1201, null);
    }

    @Override
    public Pair<Integer, List<ReplicationController>> findReplicationControllersByNamespace(String namespace) {
        try{
            List<ReplicationController> items = KubernetesUtils.client.replicationControllers().inNamespace(namespace).list().getItems();
            return Pair.of(1200, items);
        }catch(Exception e){
            System.out.println("获取ReplicationControllers失败，在ReplicationControllersServiceImpl类的findReplicationControllersByNamespace方法中");
        }
        return Pair.of(1201, null);

    }

    @Override
    public Pair<Integer, ReplicationController> getReplicationControllerByNameAndNamespace(String name, String namespace){
        try{
            ReplicationController item = KubernetesUtils.client.replicationControllers().inNamespace(namespace).withName(name).get();
            return Pair.of(1200, item);
        }catch(Exception e){
            System.out.println("获取ReplicationController失败，在ReplicationControllersServiceImpl类的getReplicationControllerByNameAndNamespace方法中");
        }
        return Pair.of(1201, null);

    }

    @Override
    public Pair<Integer, Boolean> deleteReplicationControllerByNameAndNamespace(String name, String namespace){
        try{
            Boolean delete = KubernetesUtils.client.replicationControllers().inNamespace(namespace).withName(name).delete();
            return Pair.of(1200, delete);
        }catch(Exception e){
            System.out.println("删除ReplicationController失败，在ReplicationControllersServiceImpl类的deleteReplicationControllerByNameAndNamespace方法中");
        }
        return Pair.of(1201, null);

    }

    @Override
    public Pair<Integer, ReplicationController> loadReplicationControllerFromYaml(String path) throws FileNotFoundException {

        InputStream yamlInputStream = byPath(path);
        try{
            ReplicationController replicationController = KubernetesUtils.client.replicationControllers().load(yamlInputStream).get();
            return Pair.of(1200, replicationController);
        }catch(Exception e){
            System.out.println("加载ReplicationController失败，在ReplicationControllersServiceImpl类的loadReplicationControllerFromYaml方法中");
        }
        return Pair.of(1201, null);

    }

    @Override
    public Pair<Integer, Boolean> createOrReplaceReplicationControllerByYamlString(String yaml){
        try{
            ReplicationController replicationController = Yaml.loadAs(yaml, ReplicationController.class);
            KubernetesUtils.client.replicationControllers().inNamespace(replicationController.getMetadata().getNamespace()).withName(replicationController.getMetadata().getName()).createOrReplace(replicationController);
            return Pair.of(1200, true);
        }catch (Exception e){
            System.out.println("创建 ReplicationController 失败，请检查 Yaml 格式或是否重名，在 ReplicationControllersServiceImpl 类的 createOrReplaceReplicationControllerByYamlString 方法中");
        }
        return Pair.of(1201, null);
    }

    @Override
    public ReplicationController createReplicationControllerByYaml(String path) throws FileNotFoundException {

        InputStream yamlInputStream = byPath(path);
        try {
            ReplicationController replicationController = KubernetesUtils.client.replicationControllers().load(yamlInputStream).get();
            String nameSpace = replicationController.getMetadata().getNamespace();
            replicationController = KubernetesUtils.client.replicationControllers().inNamespace(nameSpace).create(replicationController);
            return replicationController;
        }catch(Exception e){
            System.out.println("创建ReplicationController失败，缺少必要的命名空间参数，或是已经有相同的资源对象，在ReplicationControllersServiceImpl类的createReplicationControllerByYaml方法");
        }
        return null;
    }

    @Override
    public ReplicationController createOrReplaceReplicationController(String path) throws FileNotFoundException {
        InputStream yamlInputStream = byPath(path);
        try {
            ReplicationController replicationController = KubernetesUtils.client.replicationControllers().load(yamlInputStream).get();
            String nameSpace = replicationController.getMetadata().getNamespace();
            replicationController = KubernetesUtils.client.replicationControllers().inNamespace(nameSpace).createOrReplace(replicationController);
            return replicationController;
        }catch(Exception e){
            System.out.println("创建ReplicationController失败，缺少必要的命名空间参数，或是已经有相同的资源对象，在ReplicationControllersServiceImpl类的createOrReplaceReplicationController方法");
        }
        return null;
       }


    @Override
    public Pair<Integer, Boolean> setReplicas(String name, String namespace, Integer replicas){
        try {
            KubernetesUtils.client.replicationControllers().inNamespace(namespace).withName(name).edit().getSpec().setReplicas(replicas);
            return Pair.of(1200, true);
        }catch (Exception e){
            System.out.println("设置ReplicationController的Replicas失败，在ReplicationControllersServiceImpl类的setReplicas方法中");
        }
        return Pair.of(1201, null);
    }

    @Override
    public Pair<Integer, String> getReplicationControllerYamlByNameAndNamespace(String name ,String namespace){
        try{
            ReplicationController item = KubernetesUtils.client.replicationControllers().inNamespace(namespace).withName(name).get();
            return Pair.of(1200, Yaml.dump(item));
        }catch(Exception e){
            System.out.println("获取Yaml失败，在ReplicationControllersServiceImpl类的getReplicationControllerYamlByNameAndNamespace方法中");
        }
        return Pair.of(1201, null);

    }

    @Override
    public Pair<Integer, List<Pod>> getPodReplicationControllerInvolved(String name, String namespace){
        List<Pod> pods = new ArrayList<>();
        try{
            //获取 ReplicationController
            ReplicationController aReplicationController = KubernetesUtils.client.replicationControllers().inNamespace(namespace).withName(name).get();
            Map<String, String> matchLabels = aReplicationController.getSpec().getSelector();
            String uid = aReplicationController.getMetadata().getUid();
            //获取 Pods
            PodsServiceImpl podsService = new PodsServiceImpl();
            pods = FilterPodsByControllerUid.filterPodsByControllerUid(uid, podsService.findPodsByLabels(matchLabels));
            return Pair.of(1200, pods);
        }catch (Exception e){
            System.out.println("获取Resources失败，未获取到相应 Pod，在ReplicationControllersServiceImpl类的getPodReplicationControllerInvolved方法中");
        }
        return Pair.of(1201, null);
    }

    @Override
    public Pair<Integer, Map> getReplicationControllerResources(String name, String namespace){
        try{
            //获取 ReplicationController
            Pair<Integer, ReplicationController> pair = this.getReplicationControllerByNameAndNamespace(name, namespace);

            if(pair.getLeft() != 1200) return Pair.of(1201, null);
            else if(pair.getLeft() == 1200 && pair.getRight() == null) return Pair.of(1202, null);
            ReplicationController replicationController = pair.getRight();

            String uid = replicationController.getMetadata().getUid();
            Map<String, String> matchLabel = replicationController.getSpec().getSelector();

            //获取 Pods
            PodsServiceImpl podsService = new PodsServiceImpl();
            List<Pod> pods = FilterPodsByControllerUid.filterPodsByControllerUid(uid, podsService.findPodsByLabels(matchLabel));

            //获取 Services
            ServicesServiceImpl servicesService = new ServicesServiceImpl();
            List<io.fabric8.kubernetes.api.model.Service> services = servicesService.getServicesByLabels(matchLabel);

            //获取事件
            List<Event> events = CommonServiceImpl.getEventByInvolvedObjectUid(replicationController.getMetadata().getUid());

            //封装数据
            Map<String, Object> data = new HashMap<>();
            int flag = 0;//标记哪个数据没获取到
            data.put("replicationController", replicationController);
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
            if(events != null) {
                data.put("events",events);
            }else{
                data.put("events",null);
                flag |= 1;
            }
            data.put("flag", flag);

            if(flag > 0) return Pair.of(1203, data);
            return Pair.of(1200, data);
        }catch (Exception e){
            System.out.println("请求失败，在 ReplicationControllersServiceImpl 类的 getReplicationControllerResources 方法中");
        }
        return Pair.of(1201, null);
    }
}

package com.nwu.service.workload.impl;

import com.nwu.service.explorebalancing.impl.ServicesServiceImpl;
import com.nwu.service.impl.CommonServiceImpl;
import com.nwu.service.workload.DaemonSetsService;
import com.nwu.util.FilterPodsByControllerUid;
import com.nwu.util.KubernetesUtils;
import com.nwu.util.format.PodFormat;
import io.fabric8.kubernetes.api.model.Event;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.apps.DaemonSet;
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
 * Daemon Sets 的 service 层实现类
 */
@Service
public class DaemonSetsServiceImpl implements DaemonSetsService {
    @Override
    public Pair<Integer, List<DaemonSet>> findAllDaemonSets(){
        try{
            List<DaemonSet> items = KubernetesUtils.client.apps().daemonSets().inAnyNamespace().list().getItems();
            return Pair.of(1200, items);
        }catch(Exception e){
            System.out.println("获取DaemonSets失败，在DaemonSetsServiceImpl类的findAllDaemonSets方法中");
        }
        return Pair.of(1201, null);
    }

    @Override
    public Pair<Integer, List<DaemonSet>> findDaemonSetsByNamespace(String namespace) {
        try{
            List<DaemonSet> items = KubernetesUtils.client.apps().daemonSets().inNamespace(namespace).list().getItems();
            return Pair.of(1200, items);
        }catch(Exception e){
            System.out.println("获取DaemonSets失败，在DaemonSetsServiceImpl类的findDaemonSetsByNamespace方法中");
        }
        return  Pair.of(1201, null);
    }

    @Override
    public Pair<Integer, Boolean> deleteDaemonSetByNameAndNamespace(String name, String namespace){
        try{
            Boolean delete = KubernetesUtils.client.apps().daemonSets().inNamespace(namespace).withName(name).delete();
            return Pair.of(1200, delete);
        }catch(Exception e){
            System.out.println("删除DaemonSet失败，在DaemonSetsServiceImpl类的deleteDaemonSetByNameAndNamespace方法中");
        }
        return Pair.of(1201, null);
    }

    @Override
    public Pair<Integer, DaemonSet> loadDaemonSetFromYaml(String path) throws FileNotFoundException {
        try{
            InputStream yamlInputStream = byPath(path);
            DaemonSet daemonSet = KubernetesUtils.client.apps().daemonSets().load(yamlInputStream).get();
            return Pair.of(1200, daemonSet);
        }catch (Exception e){
            System.out.println("加载DaemonSet失败，在DaemonSetsServiceImpl类的loadDaemonSetFromYaml方法中");
        }
        return Pair.of(1201, null);
    }

    @Override
    public Pair<Integer, Boolean> createOrReplaceDaemonSetByYamlString(String yaml){
        try{
            DaemonSet daemonSet = Yaml.loadAs(yaml, DaemonSet.class);
            KubernetesUtils.client.apps().daemonSets().inNamespace(daemonSet.getMetadata().getNamespace()).withName(daemonSet.getMetadata().getName()).createOrReplace(daemonSet);
            return Pair.of(1200, true);
        }catch (Exception e){
            System.out.println("创建 DaemonSet 失败，请检查 Yaml 格式或是否重名，在 DaemonSetsServiceImpl 类的 createOrReplaceDaemonSetByYamlString 方法中");
        }
        return Pair.of(1201, null);
    }

    @Override
    public DaemonSet createOrReplaceDaemonSet(String path) throws FileNotFoundException {
        InputStream yamlInputStream = byPath(path);
        try {
            DaemonSet daemonSet = KubernetesUtils.client.apps().daemonSets().load(yamlInputStream).get();
            String namespace = daemonSet.getMetadata().getNamespace();
            daemonSet = KubernetesUtils.client.apps().daemonSets().inNamespace(namespace).createOrReplace(daemonSet);
            return daemonSet;
        }catch(Exception e){
            System.out.println("获取DaemonSet失败，缺少必要的命名空间参数，或是已经有相同的资源对象，在DaemonSetsServiceImpl类的createOrReplaceDaemonSet方法");
        }
        return null;
    }

    @Override
    public Boolean createOrReplaceDaemonSetByYaml(String yaml) {
        try {
            DaemonSet daemonSet = KubernetesUtils.client.apps().daemonSets().load(yaml).get();
            String namespace = daemonSet.getMetadata().getNamespace();
            KubernetesUtils.client.apps().daemonSets().inNamespace(namespace).createOrReplace(daemonSet);
            return true;
        }catch(Exception e){
            System.out.println("获取DaemonSet失败，缺少必要的命名空间参数，或是已经有相同的资源对象，在DaemonSetsServiceImpl类的createOrReplaceDaemonSet方法");
        }
        return null;
    }

    @Override
    public Pair<Integer, DaemonSet> getDaemonSetByNameAndNamespace(String name, String namespace){
        try{
            DaemonSet item = KubernetesUtils.client.apps().daemonSets().inNamespace(namespace).withName(name).get();
            return Pair.of(1200, item);
        }catch (Exception e){
            System.out.println("获取DaemonSet失败，在DaemonSetsServiceImpl类的getDaemonSetByNameAndNamespace方法中");
        }
        return Pair.of(1201, null);
    }

    @Override
    public Pair<Integer, String> getDaemonSetYamlByNameAndNamespace(String name ,String namespace){
        try{
            DaemonSet item = KubernetesUtils.client.apps().daemonSets().inNamespace(namespace).withName(name).get();
            return Pair.of(1200, Yaml.dump(item));
        }catch(Exception e){
            System.out.println("获取Yaml失败，在DaemonSetsServiceImpl类的getDaemonSetYamlByNameAndNamespace方法中");
        }
        return Pair.of(1201, null);
    }

    @Override
    public Pair<Integer, List<Pod>> getPodDaemonSetInvolved(String name, String namespace){
        try{
            //获取 DaemonSet
            DaemonSet aDaemonSet = KubernetesUtils.client.apps().daemonSets().inNamespace(namespace).withName(name).get();
            Map<String, String> matchLabels = aDaemonSet.getSpec().getSelector().getMatchLabels();
            String uid = aDaemonSet.getMetadata().getUid();
            List<Pod> pods = new ArrayList<>();
            //获取 Pods
            PodsServiceImpl podsService = new PodsServiceImpl();
            pods = FilterPodsByControllerUid.filterPodsByControllerUid(uid, podsService.findPodsByLabels(matchLabels));
            return Pair.of(1200, pods);
        }catch (Exception e){
            System.out.println("获取Resources失败，未获取到相应，在DaemonSetsServiceImpl类的getPodDaemonSetInvolved方法中");
        }
        return Pair.of(1201, null);
    }

    @Override
    public Pair<Integer, Map> getDaemonSetResources(String name, String namespace){
        try{
            Pair<Integer, DaemonSet> pair = this.getDaemonSetByNameAndNamespace(name, namespace);

            if(pair.getLeft() != 1200) return Pair.of(1201, null);// 操作失败
            else if(pair.getLeft() == 1200 && pair.getRight() == null) return Pair.of(1202, null);// 非法操作
            DaemonSet daemonSet = pair.getRight();

            //获取 DaemonSet 有关的 Pods
            Pair<Integer, List<Pod>> pairPods = this.getPodDaemonSetInvolved(name, namespace);

            //获取 Services
            ServicesServiceImpl servicesService = new ServicesServiceImpl();
            List<io.fabric8.kubernetes.api.model.Service> services = servicesService.getServicesByLabels(daemonSet.getSpec().getSelector().getMatchLabels());

            //获取事件
            List<Event> events = CommonServiceImpl.getEventByInvolvedObjectUid(daemonSet.getMetadata().getUid());

            //封装数据
            Map<String, Object> data = new HashMap<>();
            int flag = 0;//标记哪个数据没获取到
            data.put("daemonSet", daemonSet);
            if(pairPods.getLeft() == 1200) {
                data.put("pods", PodFormat.formatPodList(pairPods.getRight()));
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
        }catch(Exception e){
            System.out.println("请求失败，在 DaemonSetsServiceImpl 类的 getDaemonSetResources 方法中");
        }
        return Pair.of(1201, null);
    }
}

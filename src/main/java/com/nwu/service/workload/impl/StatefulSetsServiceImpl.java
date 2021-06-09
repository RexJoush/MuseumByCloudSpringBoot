package com.nwu.service.workload.impl;

import com.nwu.service.impl.CommonServiceImpl;
import com.nwu.service.workload.StatefulSetsService;
import com.nwu.util.FilterPodsByControllerUid;
import com.nwu.util.KubernetesUtils;
import com.nwu.util.format.PodFormat;
import io.fabric8.kubernetes.api.model.Event;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.apps.StatefulSet;
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
 * Stateful Sets 的 service 层实现类
 */
@Service
public class StatefulSetsServiceImpl implements StatefulSetsService {
    @Override
    public Pair<Integer, List<StatefulSet>> findAllStatefulSets(){
        try{
            List<StatefulSet> items = KubernetesUtils.client.apps().statefulSets().inAnyNamespace().list().getItems();
            return Pair.of(1200, items);
        }catch(Exception e){
            System.out.println("获取 StatefulSets 失败，在 StatefulSetsServiceImpl  类的findAllStatefulSets方法中");
        }
        return Pair.of(1201, null);


    }

    @Override
    public Pair<Integer, List<StatefulSet>> findStatefulSetsByNamespace(String namespace) {
        try{
            List<StatefulSet> items = KubernetesUtils.client.apps().statefulSets().inNamespace(namespace).list().getItems();
            return Pair.of(1200, items);
        }catch(Exception e){
            System.out.println("获取 StatefulSets 失败，在 StatefulSetsServiceImpl  类的findStatefulSetsByNamespace方法中");
        }
        return Pair.of(1201, null);

    }

    @Override
    public Pair<Integer, StatefulSet> getStatefulSetByNameAndNamespace(String name, String namespace){
        try{
            StatefulSet item = KubernetesUtils.client.apps().statefulSets().inNamespace(namespace).withName(name).get();
            return Pair.of(1200, item);
        }catch(Exception e){
            System.out.println("获取 StatefulSet 失败，在 StatefulSetsServiceImpl  类的getStatefulSetByNameAndNamespace方法中");
        }
        return Pair.of(1201, null);

    }

    @Override
    public Pair<Integer, Boolean> deleteStatefulSetByNameAndNamespace(String name, String namespace){
        try{
            Boolean delete = KubernetesUtils.client.apps().statefulSets().inNamespace(namespace).withName(name).delete();
            return Pair.of(1200, delete);
        }catch(Exception e){
            System.out.println("删除 StatefulSet 失败，在 StatefulSetsServiceImpl  类的deleteStatefulSetByNameAndNamespace方法中");
        }
        return Pair.of(1201, null);

    }

    @Override
    public Pair<Integer, StatefulSet> loadStatefulSetFromYaml(String path) throws FileNotFoundException {

        InputStream yamlInputStream = byPath(path);
        try{
            StatefulSet statefulSet = KubernetesUtils.client.apps().statefulSets().load(yamlInputStream).get();
            return Pair.of(1200, statefulSet);
        }catch(Exception e){
            System.out.println("加载 StatefulSet 失败，在 StatefulSetsServiceImpl  类的loadStatefulSetFromYaml方法中");
        }
        return Pair.of(1201, null);

    }

    @Override
    public Pair<Integer, Boolean> createOrReplaceStatefulSetByYamlString(String yaml){
        try{
            StatefulSet statefulSet = Yaml.loadAs(yaml, StatefulSet.class);
            KubernetesUtils.client.apps().statefulSets().inNamespace(statefulSet.getMetadata().getNamespace()).withName(statefulSet.getMetadata().getName()).createOrReplace(statefulSet);
            return Pair.of(1200, true);
        }catch (Exception e){
            System.out.println("创建 StatefulSet 失败，请检查 Yaml 格式或是否重名，在 StatefulSetsServiceImpl 类的 createOrReplaceStatefulSetByYamlString 方法中");
        }
        return Pair.of(1201, null);
    }

    @Override
    public StatefulSet createStatefulSetByYaml(String path) throws FileNotFoundException {

        InputStream yamlInputStream = byPath(path);

        try {
            StatefulSet statefulSet = KubernetesUtils.client.apps().statefulSets().load(yamlInputStream).get();
            String nameSpace = statefulSet.getMetadata().getNamespace();
            statefulSet = KubernetesUtils.client.apps().statefulSets().inNamespace(nameSpace).create(statefulSet);
            return statefulSet;
        }catch(Exception e){
            System.out.println("创建 StatefulSet 失败，缺少必要的命名空间参数，或是已经有相同的资源对象，在 StatefulSetsServiceImpl  类的createStatefulSetByYaml方法");
        }
        return null;
    }

    @Override
    public StatefulSet createOrReplaceStatefulSet(String path) throws FileNotFoundException {

        InputStream yamlInputStream = byPath(path);


        try {
            StatefulSet statefulSet = KubernetesUtils.client.apps().statefulSets().load(yamlInputStream).get();
            String nameSpace = statefulSet.getMetadata().getNamespace();
            statefulSet = KubernetesUtils.client.apps().statefulSets().inNamespace(nameSpace).createOrReplace(statefulSet);
            return statefulSet;
        }catch(Exception e){
            System.out.println("创建 StatefulSet 失败，缺少必要的命名空间参数，或是已经有相同的资源对象，在 StatefulSetsServiceImpl  类的createOrReplaceStatefulSet方法");
        }
        return null;
    }

    @Override
    public Pair<Integer, String> getStatefulSetYamlByNameAndNamespace(String name ,String namespace){
        try{
            StatefulSet item = KubernetesUtils.client.apps().statefulSets().inNamespace(namespace).withName(name).get();
            return Pair.of(1200, Yaml.dump(item));
        }catch(Exception e){
            System.out.println("获取 Yaml 失败，在 StatefulSetsServiceImpl  类的getStatefulSetYamlByNameAndNamespace方法中");
        }
        return Pair.of(1201, null);

    }

    @Override
    public Pair<Integer, List<Pod>> getPodStatefulSetInvolved(String name, String namespace){
        List<Pod> pods = new ArrayList<>();
        try{
            //获取 StatefulSet
            StatefulSet aStatefulSet = KubernetesUtils.client.apps().statefulSets().inNamespace(namespace).withName(name).get();
            Map<String, String> matchLabels = aStatefulSet.getSpec().getSelector().getMatchLabels();
            String uid = aStatefulSet.getMetadata().getUid();
            //获取 Pods
            PodsServiceImpl podsService = new PodsServiceImpl();
            pods = FilterPodsByControllerUid.filterPodsByControllerUid(uid, podsService.findPodsByLabels(matchLabels));
            return Pair.of(1200, pods);
        }catch (Exception e){
            System.out.println("获取 Resources 失败，在 StatefulSetsServiceImpl  类的getPodStatefulSetInvolved方法中");
        }
        return Pair.of(1201, null);
    }

    @Override
    public Pair<Integer, Map> getStatefulSetResources(String name, String namespace){
        try{
            //获取 StatefulSet
            Pair<Integer, StatefulSet> pair = this.getStatefulSetByNameAndNamespace(name, namespace);

            if(pair.getLeft() != 1200) return Pair.of(1201, null);
            else if(pair.getLeft() == 1200 && pair.getRight() == null) return Pair.of(1202, null);
            StatefulSet statefulSet = pair.getRight();

            String uid = statefulSet.getMetadata().getUid();
            Map<String, String> matchLabels = statefulSet.getSpec().getSelector().getMatchLabels();

            //获取 Pods
            PodsServiceImpl podsService = new PodsServiceImpl();
            List<Pod> pods = FilterPodsByControllerUid.filterPodsByControllerUid(uid, podsService.findPodsByLabels(matchLabels));

            //获取事件
            Pair<Integer, List<Event>> pairOfEvents = CommonServiceImpl.getEventByInvolvedObjectUid(statefulSet.getMetadata().getUid());

            //封装数据
            Map<String, Object> data = new HashMap<>();
            int flag = 0;//标记哪个数据没获取到
            data.put("statefulSet", statefulSet);
            if(pods != null) {
                data.put("pods", PodFormat.formatPodList(pods));
            }else {
                data.put("pods", null);
                flag |= (1 << 1);
            }
            if(pairOfEvents.getRight() != null) {
                data.put("events", pairOfEvents.getRight());
            }else{
                data.put("events", null);
                flag |= 1;
            }
            data.put("flag", flag);

            if(flag > 0) return Pair.of(1203, data);
            return Pair.of(1200, data);
        }catch (Exception e){
            System.out.println("请求失败，在 StatefulSetsServiceImpl 类的 getStatefulSetResources 方法中");
        }
        return Pair.of(1201, null);
    }
}

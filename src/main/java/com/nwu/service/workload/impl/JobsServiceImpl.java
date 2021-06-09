package com.nwu.service.workload.impl;

import com.nwu.service.impl.CommonServiceImpl;
import com.nwu.service.workload.JobsService;
import com.nwu.util.FilterPodsByControllerUid;
import com.nwu.util.KubernetesUtils;
import com.nwu.util.format.PodFormat;
import io.fabric8.kubernetes.api.model.Event;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.batch.Job;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.util.Yaml;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.IOException;
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
 * Jobs 的 service 层实现类
 */
@Service
public class JobsServiceImpl implements JobsService {
    @Override
    public Pair<Integer, List<Job>> findAllJobs(){
        try{
            List<Job> items = KubernetesUtils.client.batch().jobs().inAnyNamespace().list().getItems();
            return Pair.of(1200, items);
        }catch(Exception e){
            System.out.println("获取Jobs失败，在JobsServiceImpl类的findAllJobs方法中");
        }
        return Pair.of(1201, null);
    }

    @Override
    public Pair<Integer, List<Job>> findJobsByNamespace(String namespace) {
        try{
            List<Job> items = KubernetesUtils.client.batch().jobs().inNamespace(namespace).list().getItems();
            return Pair.of(1200, items);
        }catch(Exception e){
            System.out.println("获取Jobs失败，在JobsServiceImpl类的findJobsByNamespace方法中");
        }
        return Pair.of(1201, null);
    }

    @Override
    public Pair<Integer, Job> getJobByNameAndNamespace(String name, String namespace){
        try{
            Job item = KubernetesUtils.client.batch().jobs().inNamespace(namespace).withName(name).get();
            return Pair.of(1200, item);
        }catch(Exception e){
            System.out.println("获取Job失败，在JobsServiceImpl类的getJobByNameAndNamespace方法中");
        }
        return Pair.of(1201, null);
    }

    @Override
    public Pair<Integer, Boolean> deleteJobByNameAndNamespace(String name, String namespace){
        try{
            Boolean delete = KubernetesUtils.client.batch().jobs().inNamespace(namespace).withName(name).delete();
            return Pair.of(1200, delete);
        }catch(Exception e){
            System.out.println("删除Job失败，在JobsServiceImpl类的deleteJobByNameAndNamespace方法中");
        }
        return Pair.of(1201, null);
    }

    @Override
    public Pair<Integer, Job> loadJobFromYaml(String yaml) throws FileNotFoundException {
        try{
            Job job = KubernetesUtils.client.batch().jobs().load(yaml).get();
            return Pair.of(1200, job);
        }catch(Exception e){
            System.out.println("加载Job失败，在JobsServiceImpl类的loadJobFromYaml方法中");
        }
        return Pair.of(1201, null);
    }

    @Override
    public Pair<Integer, Boolean> createOrReplaceJobByYamlString(String yaml) throws IOException, ApiException {
        try{
            Job job = Yaml.loadAs(yaml, Job.class);
            job.getSpec().setManualSelector(true);
            KubernetesUtils.client.batch().jobs().inNamespace(job.getMetadata().getNamespace()).withName(job.getMetadata().getName()).createOrReplace(job);
            return Pair.of(1200, true);
        }catch (Exception e){
            System.out.println("创建 Job 失败，请检查 Yaml 格式或是否重名，在 JobsServiceImpl 类的 createOrReplaceJobByYamlString 方法中");
        }
        return Pair.of(1201, null);
    }

    @Override
    public Job createJobByYaml(String path) throws FileNotFoundException {
        InputStream yamlInputStream = byPath(path);
        try {
            Job job = KubernetesUtils.client.batch().jobs().load(yamlInputStream).get();
            String nameSpace = job.getMetadata().getNamespace();
            job = KubernetesUtils.client.batch().jobs().inNamespace(nameSpace).create(job);
            return job;
        }catch(Exception e){
            System.out.println("创建Job失败，缺少必要的命名空间参数，或是已经有相同的资源对象，在JobsServiceImpl类的createJobByYaml方法");
        }
        return null;
    }

    @Override
    public Job createOrReplaceJob(String path) throws FileNotFoundException {
        InputStream yamlInputStream = byPath(path);
        try {
            Job job = KubernetesUtils.client.batch().jobs().load(yamlInputStream).get();
            String nameSpace = job.getMetadata().getNamespace();
            job = KubernetesUtils.client.batch().jobs().inNamespace(nameSpace).createOrReplace(job);
            return job;
        }catch(Exception e){
            System.out.println("创建Job失败，缺少必要的命名空间参数，或是已经有相同的资源对象，在JobsServiceImpl类的createOrReplaceJob方法");
        }
        return null;
    }

    @Override
    public Pair<Integer, String> getJobYamlByNameAndNamespace(String name ,String namespace){

        try {
             Job item = KubernetesUtils.client.batch().jobs().inNamespace(namespace).withName(name).get();
             return Pair.of(1200, Yaml.dump(item));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("获取Yaml失败，在JobsServiceImpl类的getJobYamlByNameAndNamespace方法中");
        }
        return Pair.of(1201, null);
    }

    @Override
    public Pair<Integer, List<Pod>> getPodJobInvolved(String name, String namespace){
        try{
            List<Pod> pods = new ArrayList<>();
            //获取 Job
            Job aJob = KubernetesUtils.client.batch().jobs().inNamespace(namespace).withName(name).get();
            Map<String, String> matchLabels = aJob.getSpec().getSelector().getMatchLabels();
            String uid = aJob.getMetadata().getUid();
            //获取 Pods
            PodsServiceImpl podsService = new PodsServiceImpl();
            pods = FilterPodsByControllerUid.filterPodsByControllerUid(uid, podsService.findPodsByLabels(matchLabels));
            return Pair.of(1200, pods);
        }catch (Exception e){
            System.out.println("获取Resources失败，未获取到相应 Pod，在JobsServiceImpl类的getPodJobInvolved方法中");
        }
        return Pair.of(1201, null);
    }

    @Override
    public Pair<Integer, Map> getJobResources(String name, String namespace){
        try{
            Pair<Integer, Job> pair = this.getJobByNameAndNamespace(name, namespace);

            if(pair.getLeft() != 1200) return Pair.of(1201, null);// 操作失败
            else if(pair.getLeft() == 1200 && pair.getRight() == null) return Pair.of(1202, null);// 非法操作
            Job aJob = pair.getRight();

            PodsServiceImpl podsService = new PodsServiceImpl();
            Map<String, String> matchLabels = aJob.getSpec().getSelector().getMatchLabels();
            List<Pod> pods = podsService.findPodsByLabels(matchLabels);

            //获取事件
            List<Event> events = CommonServiceImpl.getEventByInvolvedObjectUid(aJob.getMetadata().getUid());

            Map<String, Object> data = new HashMap<>();

            int flag = 0;//标记哪个数据没获取到
            data.put("job", aJob);
            if(pods != null) {
                data.put("pods", PodFormat.formatPodList(pods));
            }else {
                data.put("pods", null);
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
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("请求失败，在 JobsServiceImpl 类的 getJobResources 方法中");
        }
        return Pair.of(1201, null);
    }
}

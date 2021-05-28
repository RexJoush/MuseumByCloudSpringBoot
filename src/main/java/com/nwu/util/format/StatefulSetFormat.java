package com.nwu.util.format;

import com.nwu.entity.workload.JobInformation;
import com.nwu.entity.workload.StatefulSetInformation;
import com.nwu.service.workload.impl.PodsServiceImpl;
import com.nwu.util.FilterPodsByControllerUid;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.apps.StatefulSet;
import io.fabric8.kubernetes.api.model.batch.Job;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author zqy
 * @time 2021.04.16
 */
public class StatefulSetFormat {
    public static List<StatefulSetInformation> formatStatefulSetList(List<StatefulSet> statefulSetList){

        List<StatefulSetInformation> statefulSetInformationList = new ArrayList<>();


        Iterator<StatefulSet> iterator = statefulSetList.iterator();
        List<Integer> test = new ArrayList<>();
        while(iterator.hasNext()){

            //获取 StatefulSet
            StatefulSet aStatefulSet = iterator.next();
//            Map<String, String> matchLabels = aStatefulSet.getSpec().getSelector().getMatchLabels();

            //获取 StatefulSet 对应 Pods
//            PodsServiceImpl podsService = new PodsServiceImpl();
//            List<Pod> pods = podsService.findPodsByLabels(matchLabels);
//            pods = FilterPodsByControllerUid.filterPodsByControllerUid(aStatefulSet.getMetadata().getUid(), pods);
//
//            String status = "1";
//            int runningPods = 0;
//            for(int i = 0; i < pods.size(); i ++){
//                Pod tmpPod = pods.get(i);
//                if(tmpPod.getStatus().getPhase().equals("Running")){
//                    runningPods += 1;
//                }
//                else if(tmpPod.getStatus().getPhase().equals("Pending")){
//                    status = "0";
//                }
//            }

            //标准化StatefulSet
            StatefulSetInformation statefulSetInformation = new StatefulSetInformation();
            String name = null == aStatefulSet.getMetadata().getName() ? "未知" : aStatefulSet.getMetadata().getName();
            statefulSetInformation.setName(name);
            statefulSetInformation.setNamespace(aStatefulSet.getMetadata().getNamespace() == null ? "未知" : aStatefulSet.getMetadata().getNamespace());
            statefulSetInformation.setCreationTimestamp(aStatefulSet.getMetadata().getCreationTimestamp() == null ? "未知" : aStatefulSet.getMetadata().getCreationTimestamp());
            statefulSetInformation.setStatus(aStatefulSet.getStatus().getCurrentReplicas() == null || aStatefulSet.getSpec().getReplicas() == null || aStatefulSet.getStatus().getCurrentReplicas() != aStatefulSet.getSpec().getReplicas() ? "0" : "1");
            statefulSetInformation.setRunningPods(aStatefulSet.getStatus().getCurrentReplicas() == null ? 0 : aStatefulSet.getStatus().getCurrentReplicas());
            statefulSetInformation.setReplicas(aStatefulSet.getSpec().getReplicas() == null ? 0 : aStatefulSet.getSpec().getReplicas());

            statefulSetInformationList.add(statefulSetInformation);
        }
        return statefulSetInformationList;
    }
}

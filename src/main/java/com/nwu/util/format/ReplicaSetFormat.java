package com.nwu.util.format;

import com.nwu.entity.workload.ReplicaSetInformation;
import io.fabric8.kubernetes.api.model.apps.ReplicaSet;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author zqy
 * @time 2021.04.16
 */
public class ReplicaSetFormat {
    public static List<ReplicaSetInformation> formatReplicaSetList(List<ReplicaSet> replicaSetList){
        if(replicaSetList == null) return null;
        List<ReplicaSetInformation> replicaSetInformationList = new ArrayList<>();


        Iterator<ReplicaSet> iterator = replicaSetList.iterator();

        while(iterator.hasNext()){

            //获取ReplicaSet
            ReplicaSet aReplicaSet = iterator.next();
//            Map<String, String> matchLabels = aReplicaSet.getSpec().getSelector().getMatchLabels();

            //获取ReplicaSet 对应 Pods
//            PodsServiceImpl podsService = new PodsServiceImpl();
//            List<Pod> pods = podsService.findPodsByLabels(matchLabels);
//            pods = FilterPodsByControllerUid.filterPodsByControllerUid(aReplicaSet.getMetadata().getUid(), pods);
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

            //标准化 ReplicaSet
            ReplicaSetInformation replicaSetInformation = new ReplicaSetInformation();
            replicaSetInformation.setName(aReplicaSet.getMetadata().getName() != null ? aReplicaSet.getMetadata().getName() : "未知");
            replicaSetInformation.setNamespace(aReplicaSet.getMetadata().getNamespace() == null ? "未知" : aReplicaSet.getMetadata().getNamespace());
            replicaSetInformation.setCreationTimestamp(aReplicaSet.getMetadata().getCreationTimestamp() == null ? "未知" : aReplicaSet.getMetadata().getCreationTimestamp());
            replicaSetInformation.setStatus(aReplicaSet.getStatus().getReplicas() == null || aReplicaSet.getStatus().getAvailableReplicas() == null || aReplicaSet.getStatus().getReplicas() != aReplicaSet.getStatus().getAvailableReplicas() ? "0" : "1");
            replicaSetInformation.setRunningPods(aReplicaSet.getStatus().getReadyReplicas() == null ? 0 : aReplicaSet.getStatus().getReadyReplicas());
            replicaSetInformation.setReplicas(aReplicaSet.getSpec().getReplicas() == null ? 0 : aReplicaSet.getSpec().getReplicas());

            replicaSetInformationList.add(replicaSetInformation);
        }
        return replicaSetInformationList;
    }
}

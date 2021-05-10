package com.nwu.util.format;

import com.nwu.entity.workload.JobInformation;
import com.nwu.entity.workload.ReplicationControllerInformation;
import com.nwu.service.workload.impl.PodsServiceImpl;
import com.nwu.util.FilterPodsByControllerUid;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.ReplicationController;
import io.fabric8.kubernetes.api.model.batch.Job;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author zqy
 * @time 2021.04.16
 */
public class ReplicationControllerFormat {
    public static List<ReplicationControllerInformation> formatReplicationControllerList(List<ReplicationController> replicationControllerList){

        List<ReplicationControllerInformation> replicationControllerInformationList = new ArrayList<>();


        Iterator<ReplicationController> iterator = replicationControllerList.iterator();
        List<Integer> test = new ArrayList<>();
        while(iterator.hasNext()){

            //获取 ReplicationController
            ReplicationController aReplicationController = iterator.next();
//            Map<String, String> matchLabels = aReplicationController.getSpec().getSelector();

            //获取Job 对应 Pods
//            PodsServiceImpl podsService = new PodsServiceImpl();
//            List<Pod> pods = podsService.findPodsByLabels(matchLabels);
//            pods = FilterPodsByControllerUid.filterPodsByControllerUid(aReplicationController.getMetadata().getUid(), pods);
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

            //标准化ReplicationController
            ReplicationControllerInformation replicationControllerInformation = new ReplicationControllerInformation();
            replicationControllerInformation.setName(aReplicationController.getMetadata().getName() == null ? "未知" : aReplicationController.getMetadata().getName());
            replicationControllerInformation.setNamespace(aReplicationController.getMetadata().getNamespace() == null ? "未知" : aReplicationController.getMetadata().getNamespace());
            replicationControllerInformation.setCreationTimestamp(aReplicationController.getMetadata().getCreationTimestamp() == null ? "未知" : aReplicationController.getMetadata().getCreationTimestamp());
            replicationControllerInformation.setStatus(aReplicationController.getStatus().getReplicas() == null || aReplicationController.getStatus().getAvailableReplicas() == null || aReplicationController.getStatus().getReplicas() == aReplicationController.getStatus().getAvailableReplicas() ? "0" : "1");
            replicationControllerInformation.setRunningPods(aReplicationController.getStatus().getAvailableReplicas() == null ? 0 : aReplicationController.getStatus().getAvailableReplicas());
            replicationControllerInformation.setReplicas(aReplicationController.getSpec().getReplicas() == null ? 0 : aReplicationController.getSpec().getReplicas());

            replicationControllerInformationList.add(replicationControllerInformation);
        }
        return replicationControllerInformationList;
    }
}

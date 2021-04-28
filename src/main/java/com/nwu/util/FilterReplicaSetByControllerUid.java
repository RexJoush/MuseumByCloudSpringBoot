package com.nwu.util;

import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.apps.ReplicaSet;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zqy
 * @time 2021.04.16
 */
public class FilterReplicaSetByControllerUid {
    public static List<ReplicaSet> filterReplicaSetsByControllerUid(String uid, List<ReplicaSet> ReplicaSets){
        List<ReplicaSet> newReplicaSets = new ArrayList<>();
        for(ReplicaSet replicaSet :ReplicaSets){
            int length = replicaSet.getMetadata().getOwnerReferences().size();
            for(int i = 0; i < length; i++){
                String controllerUid = replicaSet.getMetadata().getOwnerReferences().get(i).getUid();
                if(controllerUid.equals(uid)){
                    newReplicaSets.add(replicaSet);
                    break;
                }
            }
        }
        return newReplicaSets;
    }
}

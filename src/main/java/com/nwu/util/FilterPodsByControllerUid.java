package com.nwu.util;

import io.fabric8.kubernetes.api.model.Pod;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zqy
 * @time 2021.04.15
 */
public class FilterPodsByControllerUid {
    public static List<Pod> filterPodsByControllerUid(String uid, List<Pod> pods){
        if(pods == null) return null;
        List<Pod> newPods = new ArrayList<>();
        for(Pod pod :pods){
            int length = pod.getMetadata().getOwnerReferences().size();
            for(int i = 0; i < length; i++){
                String controllerUid = pod.getMetadata().getOwnerReferences().get(i).getUid();
                if(controllerUid.equals(uid)){
                    newPods.add(pod);
                    break;
                }
            }
        }
        return newPods;
    }
}

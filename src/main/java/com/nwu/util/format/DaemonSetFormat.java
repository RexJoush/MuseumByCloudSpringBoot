package com.nwu.util.format;

import com.nwu.entity.workload.DaemonSetInformation;
import io.fabric8.kubernetes.api.model.apps.DaemonSet;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zqy
 * @time 2021.04.19
 */
public class DaemonSetFormat {
    public static List<DaemonSetInformation> formatDaemonSetList(List<DaemonSet> daemonSetList){
        List<DaemonSetInformation> daemonSetInformationList = new ArrayList<>();

        for(int i = 0; i < daemonSetList.size(); i ++){
            DaemonSetInformation daemonSetInformation = new DaemonSetInformation();

            daemonSetInformation.setName(daemonSetList.get(i).getMetadata().getName() == null ? "未知" : daemonSetList.get(i).getMetadata().getName());
            daemonSetInformation.setNamespace(daemonSetList.get(i).getMetadata().getNamespace() == null ? "未知" : daemonSetList.get(i).getMetadata().getNamespace());
            daemonSetInformation.setStatus(daemonSetList.get(i).getStatus().getNumberAvailable() < daemonSetList.get(i).getStatus().getNumberReady() ? "0" : "1");
            daemonSetInformation.setRunningPods(daemonSetList.get(i).getStatus().getNumberReady() == null ? 0 : daemonSetList.get(i).getStatus().getNumberReady());
            daemonSetInformation.setReplicas(daemonSetList.get(i).getStatus().getNumberAvailable() == null ? 0 : daemonSetList.get(i).getStatus().getNumberAvailable());
            daemonSetInformation.setCreationTimestamp(daemonSetList.get(i).getMetadata().getCreationTimestamp() == null ? "未知" : daemonSetList.get(i).getMetadata().getCreationTimestamp());

            daemonSetInformationList.add(daemonSetInformation);
        }

        return daemonSetInformationList;
    }
}

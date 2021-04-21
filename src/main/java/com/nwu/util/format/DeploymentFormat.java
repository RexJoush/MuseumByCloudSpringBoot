package com.nwu.util.format;

import com.nwu.entity.workload.DaemonSetInformation;
import com.nwu.entity.workload.DeploymentInformation;
import io.fabric8.kubernetes.api.model.apps.DaemonSet;
import io.fabric8.kubernetes.api.model.apps.Deployment;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zqy
 * @time 2021.04.19
 */
public class DeploymentFormat {
    public static List<DeploymentInformation> formatDeploymentList(List<Deployment> deploymentList){
        List<DeploymentInformation> deploymentInformationList = new ArrayList<>();

        for(int i = 0; i < deploymentList.size(); i ++){
            DeploymentInformation deploymentInformation = new DeploymentInformation();

            deploymentInformation.setName(deploymentList.get(i).getMetadata().getName() == null ? "未知" : deploymentList.get(i).getMetadata().getName());
            deploymentInformation.setNamespace(deploymentList.get(i).getMetadata().getNamespace() == null ? "未知" : deploymentList.get(i).getMetadata().getNamespace());
            deploymentInformation.setStatus("True".equals(deploymentList.get(i).getStatus().getConditions().get(1).getStatus()) ? "0" : "1");
            deploymentInformation.setRunningPods(deploymentList.get(i).getStatus().getReadyReplicas() == null ? 0 : deploymentList.get(i).getStatus().getReadyReplicas());
            deploymentInformation.setReplicas(deploymentList.get(i).getSpec().getReplicas() == null ? 0 : deploymentList.get(i).getSpec().getReplicas());
            deploymentInformation.setCreationTimestamp(deploymentList.get(i).getMetadata().getCreationTimestamp() == null ? "未知" : deploymentList.get(i).getMetadata().getCreationTimestamp());

            deploymentInformationList.add(deploymentInformation);
        }

        return deploymentInformationList;
    }
}

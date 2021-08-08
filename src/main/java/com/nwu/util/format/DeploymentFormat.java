package com.nwu.util.format;

import com.nwu.entity.workload.Deployment.DeploymentInformation;
import com.nwu.util.TimeTransformation;
import io.fabric8.kubernetes.api.model.apps.Deployment;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zqy
 * @time 2021.04.19
 */
public class DeploymentFormat {
    public static List<DeploymentInformation> formatDeploymentList(List<Deployment> deploymentList){
        if(deploymentList == null) return null;
        List<DeploymentInformation> deploymentInformationList = new ArrayList<>();

        for(int i = 0; i < deploymentList.size(); i ++){
            DeploymentInformation deploymentInformation = new DeploymentInformation();

            // 基本信息
            deploymentInformation.setName(deploymentList.get(i).getMetadata().getName() == null ? "未知" : deploymentList.get(i).getMetadata().getName());
            deploymentInformation.setNamespace(deploymentList.get(i).getMetadata().getNamespace() == null ? "未知" : deploymentList.get(i).getMetadata().getNamespace());
            deploymentInformation.setStatus("True".equals(deploymentList.get(i).getStatus().getConditions().size() >= 2 ? deploymentList.get(i).getStatus().getConditions().get(1).getStatus() : "") ? "1" : "0");
            deploymentInformation.setRunningPods(deploymentList.get(i).getStatus().getReadyReplicas() == null ? 0 : deploymentList.get(i).getStatus().getReadyReplicas());
            deploymentInformation.setReplicas(deploymentList.get(i).getSpec().getReplicas() == null ? 0 : deploymentList.get(i).getSpec().getReplicas());
            deploymentInformation.setCreationTimestamp(deploymentList.get(i).getMetadata().getCreationTimestamp() == null ? "未知" : TimeTransformation.UTCToCST(deploymentList.get(i).getMetadata().getCreationTimestamp(), TimeTransformation.FORMAT));

            // 附加信息
            String alias = deploymentList.get(i).getMetadata().getAnnotations().get("alias");
            if(alias != null) deploymentInformation.setAlias(alias);
            
            deploymentInformationList.add(deploymentInformation);
        }

        return deploymentInformationList;
    }
}

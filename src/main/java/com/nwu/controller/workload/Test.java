package com.nwu.controller.workload;

import com.nwu.controller.workload.PodsController;
import com.nwu.service.workload.impl.*;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.apps.ReplicaSet;
import io.fabric8.kubernetes.api.model.batch.CronJob;
import io.kubernetes.client.openapi.ApiException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Test {

    public static void main(String[] args) throws ApiException, IOException {

        String path = Test.class.getClassLoader().getResource("RS.yaml").getPath();
        File file = new File(path);
        FileInputStream fileInputStream = new FileInputStream(file);
        System.out.println(file);
        PodsServiceImpl pod = new PodsServiceImpl();
        //Boolean isDeleted = replicaSetsService.deleteReplicaSetByNameAndNamespace("frontend", "default");

        String str = pod.getPodLogByNameAndNamespace("coredns-58cc8c89f4-88rqs", "kube-system");
        //String nameSpace = cj.getMetadata().getNamespace();
        System.out.println(str);
        //System.out.println(cj);
        fileInputStream.close();
    }
}
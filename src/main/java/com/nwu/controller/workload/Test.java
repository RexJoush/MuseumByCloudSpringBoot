package com.nwu.controller.workload;

import com.nwu.controller.workload.PodsController;
import com.nwu.service.cluster.impl.ClusterRolesServiceImpl;
import com.nwu.service.workload.impl.*;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.apps.ReplicaSet;
import io.fabric8.kubernetes.api.model.batch.CronJob;
import io.fabric8.kubernetes.api.model.rbac.ClusterRole;
import io.kubernetes.client.openapi.ApiException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class Test {

    public static void main(String[] args) throws ApiException, IOException {

        Map<String, String> test = new HashMap<String, String>();
        test.put("星期一", "Monday");
        test.put("星期二", "Tuesday");
        Set<String> set = test.keySet();
        Iterator<String> iterator = set.iterator();
        while(iterator.hasNext()){
            String key = (String) iterator.next();
            String value = test.get(key);
            System.out.println(key + value);
        }
        ClusterRolesServiceImpl clusterRolesService = new ClusterRolesServiceImpl();
        List<ClusterRole> allClusterRoles = clusterRolesService.getAllClusterRoles();

        System.out.println(allClusterRoles);

/*
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
        fileInputStream.close();*/
    }
}

/**
 * Copyright (C) 2015 Red Hat, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/*
import com.nwu.util.KubernetesConfig;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.ExecListener;
import io.fabric8.kubernetes.client.dsl.ExecWatch;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
*/
/**
 * This sample code is Java equivalent to `kubectl exec my-pod -- ls /`. It assumes that
 * a Pod with specified name exists in the cluster.
 */
/*
public class Test {
    private static final Logger logger = LoggerFactory.getLogger(Test.class);
    private static final CountDownLatch execLatch = new CountDownLatch(1);

    public static void main(String[] args) {
        try (final KubernetesClient k8s = new DefaultKubernetesClient()) {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ByteArrayOutputStream error = new ByteArrayOutputStream();

            ExecWatch execWatch = KubernetesConfig.client.pods().inNamespace("default").withName("my-pod")
                    .writingOutput(out)
                    .writingError(error)
                    .usingListener(new MyPodExecListener())
                    .exec("ls", "/");

            boolean latchTerminationStatus = execLatch.await(5, TimeUnit.SECONDS);
            if (!latchTerminationStatus) {
                logger.warn("Latch could not terminate within specified time");
            }
            logger.info("Exec Output: {} ", out);
            execWatch.close();
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
            ie.printStackTrace();
        }
    }

    private static class MyPodExecListener implements ExecListener {
        @Override
        public void onOpen(Response response) {
            logger.info("Shell was opened");
        }

        @Override
        public void onFailure(Throwable throwable, Response response) {
            logger.info("Some error encountered");
            execLatch.countDown();
        }

        @Override
        public void onClose(int i, String s) {
            logger.info("Shell Closing");
            execLatch.countDown();
        }
    }
}*/
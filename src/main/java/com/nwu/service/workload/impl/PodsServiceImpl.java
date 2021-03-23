package com.nwu.service.workload.impl;

import com.nwu.service.workload.PodsService;
import com.nwu.util.KubernetesConfig;
import io.fabric8.kubernetes.api.model.Pod;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Rex Joush
 * @time 2021.03.22
 */

/**
 *  Pods 的 service 层实现类
 */
@Service
public class PodsServiceImpl implements PodsService {
    public static void main(String[] args) {
        System.out.println(new PodsServiceImpl().findAllPods());
    }


    @Override
    public List<Pod> findAllPods(){

        List<Pod> items = KubernetesConfig.client.pods().inAnyNamespace().list().getItems();

        return items;

    }

    @Override
    public List<Pod> findPodsByNamespace(String namespace) {
        return null;
    }
}

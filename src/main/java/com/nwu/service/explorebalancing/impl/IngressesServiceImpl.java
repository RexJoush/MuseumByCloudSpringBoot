package com.nwu.service.explorebalancing.impl;

import com.nwu.service.explorebalancing.IngressesService;
import com.nwu.util.KubernetesConfig;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.extensions.Ingress;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Rex Joush
 * @time 2021.03.22
 */

/**
 * Ingresses 的 service 层实现类
 */
@Service
public class IngressesServiceImpl implements IngressesService {

    public static void main(String[] args) {
        System.out.println(new IngressesServiceImpl().findAllIngresses());
    }
    @Override
    public List<Ingress> findAllIngresses(){

        List<Ingress> items = KubernetesConfig.client.extensions().ingresses().list().getItems();

        return items;

    }


}

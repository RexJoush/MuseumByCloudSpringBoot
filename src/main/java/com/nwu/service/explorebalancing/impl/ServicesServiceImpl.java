package com.nwu.service.explorebalancing.impl;

import com.nwu.service.explorebalancing.ServicesService;
import com.nwu.service.workload.impl.PodsServiceImpl;
import com.nwu.util.KubernetesConfig;
import io.fabric8.kubernetes.api.model.Cluster;
import io.fabric8.kubernetes.api.model.KubernetesList;
import io.fabric8.kubernetes.api.model.Pod;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;

/**
 * @author Rex Joush
 * @time 2021.03.22
 */

/**
 * Services 的 service 层实现类
 */
@Service
public class ServicesServiceImpl implements ServicesService {


    @Override
    public List<io.fabric8.kubernetes.api.model.Service> findAllServices(){

        List<io.fabric8.kubernetes.api.model.Service> items = KubernetesConfig.client.services().list().getItems();

        return items;

    }
    public io.fabric8.kubernetes.api.model.Service loadServiceFromYaml(InputStream yamlInputStream){

        io.fabric8.kubernetes.api.model.Service service = KubernetesConfig.client.services().load(yamlInputStream).get();

        return service;
    }


    @Override
    public io.fabric8.kubernetes.api.model.Service createServiceYaml(InputStream yamlInputStream){

        io.fabric8.kubernetes.api.model.Service createSvc = KubernetesConfig.client.services().load(yamlInputStream).get();
        String nameSpace = createSvc.getMetadata().getNamespace();
        createSvc = KubernetesConfig.client.services().inNamespace(nameSpace).create(createSvc);

        return createSvc;
    }


    @Override
    public Boolean deleteServices(String serviceName,String namespace){

        Boolean deleteSvc = KubernetesConfig.client.services().inNamespace(namespace).withName(serviceName).delete();

        return deleteSvc;
    }
}

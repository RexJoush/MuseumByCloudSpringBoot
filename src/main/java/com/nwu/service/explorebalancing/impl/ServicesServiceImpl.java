package com.nwu.service.explorebalancing.impl;

import com.nwu.service.explorebalancing.ServicesService;
import com.nwu.service.workload.impl.PodsServiceImpl;
import com.nwu.util.KubernetesConfig;
import io.fabric8.kubernetes.api.model.Cluster;
import io.fabric8.kubernetes.api.model.KubernetesList;
import io.fabric8.kubernetes.api.model.Pod;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import static com.nwu.service.getYamlInputStream.byPath;

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


    @Override
    public List<io.fabric8.kubernetes.api.model.Service> findServicesByNamespace(String namespace){

        List<io.fabric8.kubernetes.api.model.Service> item = KubernetesConfig.client.services().inNamespace(namespace).list().getItems();

        return item;

    }

    @Override
    public io.fabric8.kubernetes.api.model.Service loadServiceFromYaml(String path) throws FileNotFoundException {

        InputStream yamlInputStream = byPath(path);

        io.fabric8.kubernetes.api.model.Service service = KubernetesConfig.client.services().load(yamlInputStream).get();

        return service;
    }


    @Override
    public io.fabric8.kubernetes.api.model.Service createServiceByYaml(String path) throws FileNotFoundException {

        InputStream yamlInputStream = byPath(path);

        io.fabric8.kubernetes.api.model.Service createSvc = KubernetesConfig.client.services().load(yamlInputStream).get();
        String nameSpace = createSvc.getMetadata().getNamespace();
        try {
            createSvc = KubernetesConfig.client.services().inNamespace(nameSpace).create(createSvc);
        }catch(Exception e){
            System.out.println("创建失败,在ServicesService类的createServiceByYaml方法中");
        }
        return createSvc;
    }

    @Override
    public io.fabric8.kubernetes.api.model.Service createOrReplaceService(String path) throws FileNotFoundException {

        InputStream yamlInputStream = byPath(path);

        io.fabric8.kubernetes.api.model.Service service = KubernetesConfig.client.services().load(yamlInputStream).get();
        String namespace = service.getMetadata().getNamespace();
        try{
            service = KubernetesConfig.client.services().inNamespace(namespace).createOrReplace(service);
        }catch (Exception e){
            System.out.println("失败，问题在ServicesService类中的creatOrReplaceService方法中");
        }

        return service;
    }

    @Override
    public Boolean deleteServicesByNameAndNamespace(String serviceName,String namespace){

        Boolean deleteSvc = KubernetesConfig.client.services().inNamespace(namespace).withName(serviceName).delete();

        return deleteSvc;
    }


}

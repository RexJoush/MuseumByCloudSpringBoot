package com.nwu.service.explorebalancing.impl;

import com.nwu.entity.settingstorage.ServiceDefinition;
import com.nwu.entity.workload.PodDefinition;
import com.nwu.service.explorebalancing.ServicesService;
import com.nwu.util.KubernetesUtils;
import com.nwu.util.format.PodFormat;
import io.fabric8.kubernetes.api.model.Endpoints;
import io.fabric8.kubernetes.api.model.Event;
import io.fabric8.kubernetes.api.model.Pod;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.nwu.util.GetYamlInputStream.byPath;

/**
 * @author Rex Joush
 * @time 2021.03.22
 */

/**
 * Services 的 service 层实现类
 */
@Service
public class ServicesServiceImpl implements ServicesService {

//    public static void main(String[] args) {
//        String name = "kubernetes";
//        String namespace = "default";
//        Endpoints endpoints = KubernetesUtils.client.endpoints().inNamespace(namespace).withName(name).get();
//        System.out.println(endpoints);
//    }


    @Override
    public List<io.fabric8.kubernetes.api.model.Service> findAllServices(){

        List<io.fabric8.kubernetes.api.model.Service> items = KubernetesUtils.client.services().list().getItems();

        return items;

    }


    @Override
    public List<io.fabric8.kubernetes.api.model.Service> findServicesByNamespace(String namespace){

        List<io.fabric8.kubernetes.api.model.Service> item = KubernetesUtils.client.services().inNamespace(namespace).list().getItems();

        return item;

    }

    @Override
    public io.fabric8.kubernetes.api.model.Service loadServiceFromYaml(String path) throws FileNotFoundException {

        InputStream yamlInputStream = byPath(path);

        io.fabric8.kubernetes.api.model.Service service = KubernetesUtils.client.services().load(yamlInputStream).get();

        return service;
    }


    @Override
    public io.fabric8.kubernetes.api.model.Service createServiceByYaml(String path) throws FileNotFoundException {

        InputStream yamlInputStream = null;
        try {
            yamlInputStream = byPath(path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        io.fabric8.kubernetes.api.model.Service createSvc = KubernetesUtils.client.services().load(yamlInputStream).get();
        String nameSpace = createSvc.getMetadata().getNamespace();
        try {
            createSvc = KubernetesUtils.client.services().inNamespace(nameSpace).create(createSvc);
        }catch(Exception e){
            throw new FileNotFoundException("创建失败,在ServicesService类的createServiceByYaml方法中");
        }
        return createSvc;
    }

    @Override
    public io.fabric8.kubernetes.api.model.Service createOrReplaceService(String path) throws FileNotFoundException {

        InputStream yamlInputStream = byPath(path);

        io.fabric8.kubernetes.api.model.Service service = KubernetesUtils.client.services().load(yamlInputStream).get();
        String namespace = service.getMetadata().getNamespace();
        try{
            service = KubernetesUtils.client.services().inNamespace(namespace).createOrReplace(service);
        }catch (Exception e){
            System.out.println("失败，问题在ServicesService类中的creatOrReplaceService方法中");
        }

        return service;
    }

    @Override
    public Boolean deleteServicesByNameAndNamespace(String serviceName,String namespace){

        Boolean deleteSvc = KubernetesUtils.client.services().inNamespace(namespace).withName(serviceName).delete();

        return deleteSvc;
    }


    @Override
    public ServiceDefinition getServiceByNameAndNamespace(String name, String namespace){

        // 获取 service
        io.fabric8.kubernetes.api.model.Service service = KubernetesUtils.client.services().inNamespace(namespace).withName(name).get();
        // 根据 service 的 selector 获取 pod
        Map<String, String> selector = service.getSpec().getSelector();
        // 格式化 pod 列表
        List<PodDefinition> podDefinitions = PodFormat.formatPodList(KubernetesUtils.client.pods().withLabels(selector).list().getItems());

        // 获取 endpoint
        Endpoints endpoints = KubernetesUtils.client.endpoints().inNamespace(namespace).withName(name).get();

        // 获取 event
        Event event = KubernetesUtils.client.v1().events().inNamespace(namespace).withName(name).get();

        // 封装对象
        ServiceDefinition serviceDefinition = new ServiceDefinition();

        serviceDefinition.setService(service);
        serviceDefinition.setPods(podDefinitions);
        serviceDefinition.setEndpoints(endpoints);
        serviceDefinition.setEvent(event);

        return serviceDefinition;
    }


    public Endpoints getEndpointBySvcNameAndNamespace(String name, String namespace){


        return KubernetesUtils.client.endpoints().inNamespace(namespace).withName(name).get();
    }

}

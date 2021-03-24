package com.nwu.controller.explorebalancing;

import com.nwu.controller.workload.PodsController;
import com.nwu.service.explorebalancing.ServicesService;
import com.nwu.service.explorebalancing.impl.ServicesServiceImpl;
import io.fabric8.kubernetes.api.model.Service;

import io.kubernetes.client.openapi.ApiException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

public class Test {

    public static void main(String[] args) throws ApiException, IOException {

//        String path = com.nwu.service.explorebalancing.ServicesService.class.getClassLoader().getResource("service.yaml").getPath();
//        File file = new File(path);
//        FileInputStream fileInputStream = new FileInputStream(file);
//
//        ServicesServiceImpl servicesServiceImpl = new ServicesServiceImpl();
////
////        Service serviceYaml = servicesServiceImpl.createServiceByYaml(fileInputStream);
//        List<Service> allService = servicesServiceImpl.findAllServices();
//        System.out.println(allService);
//        System.out.println(serviceYaml);

    }
}
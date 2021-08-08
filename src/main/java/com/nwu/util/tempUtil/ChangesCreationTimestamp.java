package com.nwu.util.tempUtil;

import com.nwu.util.TimeTransformation;
import io.fabric8.kubernetes.api.model.ConfigMap;
import io.fabric8.kubernetes.api.model.Service;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.extensions.Ingress;

import java.util.List;

public class ChangesCreationTimestamp {

    public static List<Service> serviceList(List<Service> service){
        for(int i = 0; i < service.size(); i++){
            String tmp = TimeTransformation.UTCToCST(service.get(i).getMetadata().getCreationTimestamp(), TimeTransformation.FORMAT);
            service.get(i).getMetadata().setCreationTimestamp(tmp);
        }
        return service;
    }

    public static List<Ingress> ingressList(List<Ingress> ingresses){
        for(int i = 0; i < ingresses.size(); i++){
            String tmp = TimeTransformation.UTCToCST(ingresses.get(i).getMetadata().getCreationTimestamp(), TimeTransformation.FORMAT);
            ingresses.get(i).getMetadata().setCreationTimestamp(tmp);
        }
        return ingresses;
    }

    public static List<ConfigMap> configMapList(List<ConfigMap> configMaps){
        for(int i = 0; i < configMaps.size(); i++){
            String tmp = TimeTransformation.UTCToCST(configMaps.get(i).getMetadata().getCreationTimestamp(), TimeTransformation.FORMAT);
            configMaps.get(i).getMetadata().setCreationTimestamp(tmp);
        }
        return configMaps;
    }

    public static Deployment deployment(Deployment deployment){
        String tmp = TimeTransformation.UTCToCST(deployment.getMetadata().getCreationTimestamp(), TimeTransformation.FORMAT);
        deployment.getMetadata().setCreationTimestamp(tmp);
        return deployment;
    }
}

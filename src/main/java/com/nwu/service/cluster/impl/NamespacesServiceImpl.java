package com.nwu.service.cluster.impl;

import com.nwu.service.cluster.NamespacesService;
import com.nwu.util.KubernetesUtils;
import io.fabric8.kubernetes.api.model.Namespace;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zqy
 * @time 2020.03.24
 */

@Service
public class NamespacesServiceImpl implements NamespacesService {

    @Override
    public List<Namespace> getAllNamespaces(){

        List<Namespace> items = KubernetesUtils.client.namespaces().list().getItems();

        return items;

    }

    @Override
    public Boolean deleteNamespaceByName(String namespace){

        Boolean delete = KubernetesUtils.client.namespaces().withName(namespace).delete();

        return delete;
    }

}

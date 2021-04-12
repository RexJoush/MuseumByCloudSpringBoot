package com.nwu.service.cluster.impl;

import com.nwu.entity.cluster.NamespaceName;
import com.nwu.service.cluster.NamespacesService;
import com.nwu.util.KubernetesUtils;
import io.fabric8.kubernetes.api.model.Namespace;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    public List<NamespaceName> getAllNamespaceName() {
        List<Namespace> items = KubernetesUtils.client.namespaces().list().getItems();
        List<NamespaceName> namespaceNameList = new ArrayList<>();
        namespaceNameList.add(new NamespaceName("所有命名空间", ""));
        for (Namespace item : items) {
            namespaceNameList.add(new NamespaceName(item.getMetadata().getName(), item.getMetadata().getName()));
        }

        return namespaceNameList;

    }

    @Override
    public Boolean deleteNamespaceByName(String namespace){

        Boolean delete = KubernetesUtils.client.namespaces().withName(namespace).delete();

        return delete;
    }

}

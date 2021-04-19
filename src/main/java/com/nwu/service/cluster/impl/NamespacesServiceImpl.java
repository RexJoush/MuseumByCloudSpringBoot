package com.nwu.service.cluster.impl;

import com.nwu.entity.cluster.Definition.NamespaceDefinition;
import com.nwu.entity.cluster.NamespaceDetails;
import com.nwu.entity.cluster.NamespaceName;
import com.nwu.entity.common.EventDefinition;
import com.nwu.service.cluster.NamespacesService;
import com.nwu.util.KubernetesUtils;
import io.fabric8.kubernetes.api.model.Event;
import io.fabric8.kubernetes.api.model.Namespace;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.models.V1Namespace;
import io.kubernetes.client.util.Yaml;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author zqy
 * @time 2020.03.24
 */

@Service
public class NamespacesServiceImpl implements NamespacesService {


    @Override
    public List<NamespaceDefinition> getAllNamespaces(){

        // 自定义结果集
        List<NamespaceDefinition> result = new ArrayList<>();

        // 获取命名空间
        List<Namespace> items = KubernetesUtils.client.namespaces().list().getItems();

        // 封装结果
        for (Namespace item : items) {
            NamespaceDefinition definition = new NamespaceDefinition();
            definition.setStatus("Active".equals(item.getStatus().getPhase()));
            definition.setName(item.getMetadata().getName());
            definition.setTime(item.getMetadata().getCreationTimestamp().replaceAll("[TZ]", " "));

            result.add(definition);
        }

        return result;

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
    public NamespaceDetails getNamespaceDetails(String namespace) {

        // 获取命名空间详情信息
        Namespace namespace1 = KubernetesUtils.client.namespaces().withName(namespace).get();
        // 获取事件信息
        List<Event> events = KubernetesUtils.client.v1().events().inNamespace("default").list().getItems();

        // 定义 自定义事件列表
        List<EventDefinition> eventDefinitions = new LinkedList<>();

        for (Event event : events) {
            EventDefinition definition = new EventDefinition();
            // 设置信息
            definition.setMessage(event.getMessage());
            // 设置资源
            definition.setResource(event.getSource().getComponent() + " " + event.getSource().getHost());
            // 设置子对象
            definition.setChildObject(event.getInvolvedObject().getFieldPath());
            // 设置次数
            if (event.getCount() == null){
                definition.setCount(0);
            } else {
                definition.setCount(event.getCount());
            }
            // 设置初次时间
            if (event.getFirstTimestamp() == null){
                definition.setFirstTime("");
            } else {
                definition.setFirstTime(event.getFirstTimestamp());
            }
            // 设置最后一次时间
            if (event.getLastTimestamp() == null) {
                definition.setLastTime("");
            } else {
                definition.setLastTime(event.getLastTimestamp());
            }

            eventDefinitions.add(definition);
        }

        NamespaceDetails details = new NamespaceDetails();

        details.setNamespace(namespace1);
        details.setEvents(eventDefinitions);
        return details;

    }

    @Override
    public Boolean deleteNamespaceByName(String namespace){

        Boolean delete = KubernetesUtils.client.namespaces().withName(namespace).delete();

        return delete;
    }

    @Override
    public String findNamespaceYamlByName(String name) {

        V1Namespace v1Namespace = null;

        try {
            v1Namespace = KubernetesUtils.coreV1Api.readNamespace(name,null,null,null);
        } catch (ApiException e) {
            e.printStackTrace();
        }
        return Yaml.dump(v1Namespace);
    }

}

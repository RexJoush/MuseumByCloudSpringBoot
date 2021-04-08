package com.nwu.service.impl;

import com.nwu.service.CustomizeService;
import com.nwu.util.KubernetesUtils;
import io.fabric8.kubernetes.api.model.Sysctl;
import io.fabric8.kubernetes.api.model.apiextensions.v1.CustomResourceDefinition;
import io.fabric8.kubernetes.client.dsl.base.CustomResourceDefinitionContext;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import static com.nwu.util.GetYamlInputStream.byPath;

/**
 * @author Rex Bernie
 * @time 2021.03.22
 */

/**
 * 自定义资源的 service 层实现类
 */
@Service
public class CustomizeServiceImpl implements CustomizeService {


    public CustomResourceDefinition loadCustomResourceDefinition(String path) throws FileNotFoundException {
        InputStream yamlInputStream = byPath(path);
        CustomResourceDefinition customResourceDefinition = KubernetesUtils.client.apiextensions().v1().customResourceDefinitions().load(yamlInputStream).get();
        return customResourceDefinition;
    }

    @Override
    public CustomResourceDefinition createCustomResourceDefinition(String path) throws FileNotFoundException {
        CustomResourceDefinition customResourceDefinition = KubernetesUtils.client.apiextensions().v1().customResourceDefinitions().createOrReplace(loadCustomResourceDefinition(path));
        return customResourceDefinition;
    }

    @Override
    public List<CustomResourceDefinition> getCustomResourceDefinition() {
        List<CustomResourceDefinition> items = KubernetesUtils.client.apiextensions().v1().customResourceDefinitions().list().getItems();
        return items;
    }

    @Override
    public boolean deleteCustomResourceDefinition(CustomResourceDefinition customResourceDefinition) {
        boolean deleted = KubernetesUtils.client.apiextensions().v1().customResourceDefinitions().delete(customResourceDefinition);
        return deleted;
    }

    @Override
    public Map<String, Object> getCustomResourceDefinitionObject(String nameSpace, String deviceName) {
        CustomResourceDefinitionContext context = new CustomResourceDefinitionContext
                .Builder()
                .withGroup("devices.kubeedge.io")
                .withKind("Device")
                .withName("devices.devices.kubeedge.io")
                .withPlural("devices")
                .withScope("Namespaced")
                .withVersion("v1alpha2")
                .build();
        return KubernetesUtils.client.customResource(context).get(nameSpace, deviceName);
    }

    @Override
    public Map<String, Object> getCustomResourceDefinitionObjectList(String nameSpace) {
        CustomResourceDefinitionContext context = new CustomResourceDefinitionContext
                .Builder()
                .withGroup("devices.kubeedge.io")
                .withKind("Device")
                .withName("devices.devices.kubeedge.io")
                .withPlural("devices")
                .withScope("Namespaced")
                .withVersion("v1alpha2")
                .build();
        return KubernetesUtils.client.customResource(context).list(nameSpace);
    }
    @Override
    public Map<String, Object> getCustomResourceDefinitionObjectListbyName(String crdName) throws FileNotFoundException {
        CustomResourceDefinition customResourceDefinition = new CustomizeServiceImpl().getCustomResourceDefinitionByName(crdName);
        CustomResourceDefinitionContext context = new CustomResourceDefinitionContext
                .Builder()
                .withGroup(customResourceDefinition.getSpec().getGroup())
                .withKind(customResourceDefinition.getSpec().getNames().getKind())
                .withName(customResourceDefinition.getMetadata().getName())
                .withPlural(customResourceDefinition.getSpec().getNames().getPlural())
                .withScope(customResourceDefinition.getSpec().getScope())
                .withVersion(customResourceDefinition.getSpec().getVersions().get(0).getName())
                .build();

        return KubernetesUtils.client.customResource(context).list();
    }

    @Override
    public Map<String, Object> getCustomResourceDefinitionObjectByCrdNameAndObjNameAndNamespace(String crdName, String objName,String nameSpace) throws FileNotFoundException {
        CustomResourceDefinition customResourceDefinition = new CustomizeServiceImpl().getCustomResourceDefinitionByName(crdName);
        CustomResourceDefinitionContext context = new CustomResourceDefinitionContext
                .Builder()
                .withGroup(customResourceDefinition.getSpec().getGroup())
                .withKind(customResourceDefinition.getSpec().getNames().getKind())
                .withName(customResourceDefinition.getMetadata().getName())
                .withPlural(customResourceDefinition.getSpec().getNames().getPlural())
                .withScope(customResourceDefinition.getSpec().getScope())
                .withVersion(customResourceDefinition.getSpec().getVersions().get(0).getName())
                .build();

        return KubernetesUtils.client.customResource(context).get(nameSpace,objName);
    }


    @Override
    public CustomResourceDefinition getCustomResourceDefinitionByName(String name) throws FileNotFoundException {

        CustomResourceDefinition customResourceDefinition = KubernetesUtils.client.apiextensions().v1().customResourceDefinitions().withName(name).get();
        return customResourceDefinition;
    }


}

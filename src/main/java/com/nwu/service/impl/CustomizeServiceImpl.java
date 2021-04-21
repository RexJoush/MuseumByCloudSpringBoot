package com.nwu.service.impl;

import com.nwu.service.CustomizeService;
import com.nwu.util.KubernetesUtils;
import io.fabric8.kubernetes.api.model.apiextensions.v1.CustomResourceDefinition;
import io.fabric8.kubernetes.client.dsl.base.CustomResourceDefinitionContext;
import io.kubernetes.client.util.Yaml;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
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
        return KubernetesUtils.client.apiextensions().v1().customResourceDefinitions().load(yamlInputStream).get();
    }

    @Override
    public CustomResourceDefinition createCustomResourceDefinition(String path) throws FileNotFoundException {
        return KubernetesUtils.client.apiextensions().v1().customResourceDefinitions().createOrReplace(loadCustomResourceDefinition(path));
    }

    @Override
    public List<CustomResourceDefinition> getCustomResourceDefinition() {
        return KubernetesUtils.client.apiextensions().v1().customResourceDefinitions().list().getItems();
    }

    @Override
    public boolean deleteCustomResourceDefinition(String crdName) throws FileNotFoundException {

        CustomResourceDefinition customResourceDefinition = new CustomizeServiceImpl().getCustomResourceDefinitionByName(crdName);
        return KubernetesUtils.client.apiextensions().v1().customResourceDefinitions().delete(customResourceDefinition);
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
    public Map<String, Object> getCustomResourceDefinitionObjectListByName(String crdName) throws FileNotFoundException {
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
    public String getCrdYamlByName(String crdName) {
        CustomResourceDefinition customResourceDefinition = KubernetesUtils.client.apiextensions().v1().customResourceDefinitions().withName(crdName).get();
        return Yaml.dump(customResourceDefinition);
    }

    @Override
    public String getObjectYamlByName(String crdName, String objName, String nameSpace) throws FileNotFoundException {
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
        return Yaml.dump(KubernetesUtils.client.customResource(context).get(nameSpace, objName));
    }

    @Override
    public boolean deleteCustomResourceDefinitionObject(String crdName, String objName, String nameSpace) throws IOException {
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
        Map<String, Object> delete = KubernetesUtils.client.customResource(context).delete(nameSpace, objName);
        if (delete.isEmpty()) {
            return false;
        }else {
            return true;
        }
    }

    @Override
    public Map<String, Object> getCustomResourceDefinitionObjectByCrdNameAndObjNameAndNamespace(String crdName, String objName, String nameSpace) throws FileNotFoundException {
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

        return KubernetesUtils.client.customResource(context).get(nameSpace, objName);
    }



    @Override
    public CustomResourceDefinition getCustomResourceDefinitionByName(String name) throws FileNotFoundException {

        return KubernetesUtils.client.apiextensions().v1().customResourceDefinitions().withName(name).get();
    }
}

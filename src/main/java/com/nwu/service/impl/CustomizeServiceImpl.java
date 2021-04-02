package com.nwu.service.impl;

import com.nwu.service.CustomizeService;
import com.nwu.util.KubernetesUtils;
import io.fabric8.kubernetes.api.model.apiextensions.v1.CustomResourceDefinition;
import io.fabric8.kubernetes.client.dsl.base.CustomResourceDefinitionContext;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import static com.nwu.util.GetYamlInputStream.byPath;

/**
 * @author Rex Joush
 * @time 2021.03.22
 */

/**
 * 自定义资源的 service 层实现类
 */
@Service
public class CustomizeServiceImpl implements CustomizeService {


    public CustomResourceDefinition loadCustomResourceDefinition(String path) throws FileNotFoundException {

        InputStream yamlInputStream = byPath(path);
        CustomResourceDefinition customResourceDefinition=KubernetesUtils.client.apiextensions().v1().customResourceDefinitions().load(yamlInputStream).get();
        return customResourceDefinition;
    }

    @Override
    public CustomResourceDefinition createCustomResourceDefinition(String path) throws FileNotFoundException {


        CustomResourceDefinition customResourceDefinition=KubernetesUtils.client.apiextensions().v1().customResourceDefinitions().createOrReplace(loadCustomResourceDefinition(path));
        System.out.println(customResourceDefinition);

        return customResourceDefinition;
    }

    @Override
    public List<CustomResourceDefinition> getCustomResourceDefinition() {
        List<CustomResourceDefinition> items = KubernetesUtils.client.apiextensions().v1().customResourceDefinitions().list().getItems();

        for (CustomResourceDefinition item : items) {
            System.out.println(item);
        }

        return items;
    }

    @Override
    public boolean deleteCustomResourceDefinition(CustomResourceDefinition customResourceDefinition) {
        boolean deleted= KubernetesUtils.client.apiextensions().v1().customResourceDefinitions().delete(customResourceDefinition);

        return deleted;
    }
    @Override
    public Map<String,Object> getCustomResourceDefinitionObject(String deviceName) {
        CustomResourceDefinitionContext context = new CustomResourceDefinitionContext
                .Builder()
                .withGroup("devices.kubeedge.io")
                .withKind("Device")
                .withName("devices.devices.kubeedge.io")
                .withPlural("devices")
                .withScope("Namespaced")
                .withVersion("v1alpha2")
                .build();
//        Map<String, Object> dummyObject = KubernetesUtils.client.customResource(context)
//                .load(deleteCustomResourceDefinition().class.getResourceAsStream("/test-customresource.yaml"));
        return  KubernetesUtils.client.customResource(context).get("default",deviceName);
    }

    public static void main(String[] args) {
        System.out.println(new CustomizeServiceImpl().getCustomResourceDefinitionObject("counter"));
    }
}

package com.nwu.service.impl;

import com.nwu.service.CustomizeService;
import com.nwu.util.KubernetesUtils;
import io.fabric8.kubernetes.api.model.apiextensions.v1.CustomResourceDefinition;
import io.fabric8.kubernetes.api.model.apiextensions.v1.CustomResourceDefinitionList;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

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
        return customResourceDefinition;
    }

    @Override
    public CustomResourceDefinitionList getCustomResourceDefinition() {
        CustomResourceDefinitionList crdList= KubernetesUtils.client.apiextensions().v1().customResourceDefinitions().list();
        return crdList;
    }

    @Override
    public boolean deleteCustomResourceDefinition(CustomResourceDefinition customResourceDefinition) {
        boolean deleted= KubernetesUtils.client.apiextensions().v1().customResourceDefinitions().delete(customResourceDefinition);
        return deleted;
    }
}

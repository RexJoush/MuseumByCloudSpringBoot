package com.nwu.service.impl;

/**
 * @author Rex Joush
 * @time 2021.04.07
 */

import com.nwu.service.CommonService;
import com.nwu.util.KubernetesUtils;
import io.fabric8.kubernetes.api.model.Event;
import io.fabric8.kubernetes.api.model.HasMetadata;
import io.fabric8.kubernetes.api.model.ObjectReference;
import io.fabric8.kubernetes.api.model.apiextensions.v1.CustomResourceDefinition;
import io.fabric8.kubernetes.api.model.extensions.Ingress;
import io.fabric8.kubernetes.client.dsl.base.CustomResourceDefinitionContext;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

/**
 * 公共方法 service 接口实现类
 */
@Service
public class CommonServiceImpl implements CommonService {

    @Override
    public int changeResourceByYaml(File yaml) {
        try {
            InputStream inputStream = new FileInputStream(yaml);
            List<HasMetadata> orReplace = KubernetesUtils.client.load(inputStream).createOrReplace();
            inputStream.close();
            yaml.delete();
            if (orReplace != null) {
                return 1200;
            } else {
                return 1201;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 1202;
        }
    }

    @Override
    public int changeServicesByYaml(File yaml) {

        try {
            InputStream inputStream = new FileInputStream(yaml);

            io.fabric8.kubernetes.api.model.Service service = KubernetesUtils.client.services().load(inputStream).get();

            Boolean delete = KubernetesUtils.client.services().inNamespace(service.getMetadata().getNamespace()).withName(service.getMetadata().getName()).delete();

            InputStream inputStream2 = new FileInputStream(yaml);

            List<HasMetadata> orReplace = KubernetesUtils.client.load(inputStream2).createOrReplace();

            inputStream.close();

            yaml.delete();

            if (orReplace != null) {
                return 1200;
            } else {
                return 1201;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 1202;
        }
    }

    @Override
    public int changeDeploymentByYaml(File yaml) {

        try {
            InputStream inputStream = new FileInputStream(yaml);

            io.fabric8.kubernetes.api.model.apps.Deployment orReplace1 = KubernetesUtils.client.apps().deployments().load(inputStream).get();

            Boolean delete = KubernetesUtils.client.apps().deployments().inNamespace(orReplace1.getMetadata().getNamespace()).withName(orReplace1.getMetadata().getName()).delete();

            InputStream inputStream2 = new FileInputStream(yaml);

            List<HasMetadata> orReplace = KubernetesUtils.client.load(inputStream2).createOrReplace();

            inputStream.close();

            yaml.delete();

            if (orReplace != null) {
                return 1200;
            } else {
                return 1201;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 1202;
        }
    }
    @Override
    public int changeIngressesByYaml(File yaml) {

        try {
            InputStream inputStream = new FileInputStream(yaml);

            Ingress ingress = KubernetesUtils.client.extensions().ingresses().load(inputStream).get();

            Boolean delete = KubernetesUtils.client.extensions().ingresses().inNamespace(ingress.getMetadata().getNamespace()).withName(ingress.getMetadata().getName()).delete();

            InputStream inputStream2 = new FileInputStream(yaml);

            List<HasMetadata> orReplace = KubernetesUtils.client.load(inputStream2).createOrReplace();

            inputStream.close();

            yaml.delete();

            if (orReplace != null) {
                return 1200;
            } else {
                return 1201;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 1202;
        }
    }
    @Override
    public int changeCrdObjectByYaml(File yaml,String crdName) {
        try {
            InputStream inputStream2 = new FileInputStream(yaml);

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
            List<HasMetadata> orReplace = (List<HasMetadata>) KubernetesUtils.client.customResource(context).createOrReplace(inputStream2);
            inputStream2.close();

            yaml.delete();

            if (orReplace != null) {
                return 1200;
            } else {
                return 1201;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 1202;
        }
    }

    public static Pair<Integer, List<Event>> getEventByInvolvedObjectNNK(String name, String namespace, String kind){
        try{
            ObjectReference objectReference = new ObjectReference();
            objectReference.setName(name);
            objectReference.setNamespace(namespace);
            objectReference.setKind(kind);
            List<Event> items = KubernetesUtils.client.v1().events().withInvolvedObject(objectReference).list().getItems();
            return Pair.of(1200, items);
        }catch (Exception e){
            System.out.println("获取事件失败，在 CommonServiceImpl 类的 getEventByInvolvedObjectNNK 方法里");
        }
        return Pair.of(1201, null);
    }

    public static Pair<Integer, List<Event>> getEventByInvolvedObjectUid(String uid){
        try{
            ObjectReference objectReference = new ObjectReference();
            objectReference.setUid(uid);
            List<Event> items = KubernetesUtils.client.v1().events().withInvolvedObject(objectReference).list().getItems();
            return Pair.of(1200, items);
        }catch (Exception e){
            System.out.println("获取事件失败，在 CommonServiceImpl 类的 getEventByInvolvedObjectUid 方法里");
        }
        return Pair.of(1201, null);
    }
}

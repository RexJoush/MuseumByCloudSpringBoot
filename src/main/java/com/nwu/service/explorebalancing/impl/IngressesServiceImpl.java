package com.nwu.service.explorebalancing.impl;

import com.nwu.service.explorebalancing.IngressesService;
import com.nwu.util.KubernetesUtils;
import com.nwu.util.tempUtil.ChangesCreationTimestamp;
import io.fabric8.kubernetes.api.model.HasMetadata;
import io.fabric8.kubernetes.api.model.IntOrString;
import io.fabric8.kubernetes.api.model.extensions.*;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.models.ExtensionsV1beta1Ingress;
import io.kubernetes.client.util.Yaml;
import org.checkerframework.checker.units.qual.K;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import static com.nwu.util.GetYamlInputStream.byPath;

/**
 * @author Rex Joush
 * @time 2021.03.22
 */

/**
 * Ingresses 的 service 层实现类
 */
@Service
public class IngressesServiceImpl implements IngressesService {

//    public static void main(String[] args) {
//
//        File yaml = new File("D:\\Files\\a.yaml");
//
//        try {
//            InputStream inputStream = new FileInputStream(yaml);
//
//            Ingress ingress = KubernetesUtils.client.extensions().ingresses().load(yaml).get();
//            String namespace1 = ingress.getMetadata().getNamespace();
//            String name1 = ingress.getMetadata().getName();
//            Boolean delete1 = KubernetesUtils.client.extensions().ingresses().inNamespace(namespace1).withName(name1).delete();
//            System.out.println(delete1);
//            InputStream inputStream2 = new FileInputStream(yaml);
////            List<HasMetadata> orReplace = KubernetesUtils.client.load(inputStream2).createOrReplace();
//            inputStream.close();
//            yaml.delete();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//
//    }
    @Override
    public List<Ingress> findAllIngresses(){
        List<Ingress> items = KubernetesUtils.client.extensions().ingresses().list().getItems();

        /***
         * ZQY 修改时间戳
         */
        //~~~~~~~~~~
        items = ChangesCreationTimestamp.ingressList(items);
        //~~~~~~~~~~

        return items;

    }

    @Override
    public List<Ingress> findIngressesByNamespace(String namespace){

        return KubernetesUtils.client.extensions().ingresses().inNamespace(namespace).list().getItems();
    }


    @Override
    public Ingress loadServiceFromYaml(String path) throws FileNotFoundException {

        InputStream yamlInputStream = byPath(path);

        return KubernetesUtils.client.extensions().ingresses().load(yamlInputStream).get();
    }



    @Override
    public Ingress createIngressByYaml(String path) throws FileNotFoundException {

        InputStream yamlInputStream = byPath(path);

        Ingress createIngress = KubernetesUtils.client.extensions().ingresses().load(yamlInputStream).get();

        String nameSpace = createIngress.getMetadata().getNamespace();
        try{
            createIngress = KubernetesUtils.client.extensions().ingresses().inNamespace(nameSpace).create(createIngress);
        }catch (Exception e){
            System.out.println("创建失败，在Ingress Service类的createIngressByYaml中");
        }

        return createIngress;
    }


    @Override
    public Boolean deleteIngressesByNameAndNamespace(String ingressName,String namespace){

        return KubernetesUtils.client.extensions().ingresses().inNamespace(namespace).withName(ingressName).delete();
    }

    @Override
    public Ingress createOrReplaceIngress(String path) throws FileNotFoundException {

        InputStream yamlInputStream = byPath(path);

        Ingress ingress = KubernetesUtils.client.extensions().ingresses().load(yamlInputStream).get();

        String namespace = ingress.getMetadata().getNamespace();
        try{
            ingress = KubernetesUtils.client.extensions().ingresses().createOrReplace(ingress);

        }catch (Exception e){
            System.out.println("失败，在Ingress Service中的createOrReplaceIngress方法");
        }

        return ingress;

    }

    @Override
    public Ingress getIngressByNameAndNamespace(String name, String namespace){

        return KubernetesUtils.client.extensions().ingresses().inNamespace(namespace).withName(name).get();
    }

    @Override
    public String findIngressYamlByNameAndNamespace(String name, String namespace){

//        Ingress ingress = KubernetesUtils.client.extensions().ingresses().inNamespace(namespace).withName(name).get();
//        IngressSpec spec = ingress.getSpec();
//        List<IngressRule> rules = spec.getRules();
//        IngressRule ingressRule = rules.get(0);
//        HTTPIngressRuleValue http = ingressRule.getHttp();
//        List<HTTPIngressPath> paths = http.getPaths();
//        for (HTTPIngressPath path : paths) {
//            IntOrString servicePort = path.getBackend().getServicePort();
//
//        }

        ExtensionsV1beta1Ingress extensionsV1beta1Ingress = null;
        try {
            extensionsV1beta1Ingress = KubernetesUtils.extensionsV1beta1Api.readNamespacedIngress(name, namespace, null, null, null);

        } catch (ApiException e) {
            e.printStackTrace();
        }

        return Yaml.dump(extensionsV1beta1Ingress);

    }

}

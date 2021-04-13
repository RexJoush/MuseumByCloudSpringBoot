package com.nwu.service.explorebalancing.impl;

import com.nwu.service.explorebalancing.IngressesService;
import com.nwu.util.KubernetesUtils;
import io.fabric8.kubernetes.api.model.HasMetadata;
import io.fabric8.kubernetes.api.model.extensions.Ingress;
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

    public static void main(String[] args) {

        File yaml = new File("D:\\Files\\a.yaml");

        try {
            InputStream inputStream = new FileInputStream(yaml);
//            io.fabric8.kubernetes.api.model.Service service = KubernetesUtils.client.services().load(yaml).get();
//            String namespace = service.getMetadata().getNamespace();
//            String name = service.getMetadata().getName();
//            Boolean delete = KubernetesUtils.client.services().inNamespace(namespace).withName(name).delete();
//            System.out.println(delete);
//            List<HasMetadata> orReplace = KubernetesUtils.client.load(inputStream).createOrReplace();
//            inputStream.close();

            Ingress ingress = KubernetesUtils.client.extensions().ingresses().load(yaml).get();
            String namespace1 = ingress.getMetadata().getNamespace();
            String name1 = ingress.getMetadata().getName();
            Boolean delete1 = KubernetesUtils.client.extensions().ingresses().inNamespace(namespace1).withName(name1).delete();
            System.out.println(delete1);
            InputStream inputStream2 = new FileInputStream(yaml);
//            List<HasMetadata> orReplace = KubernetesUtils.client.load(inputStream2).createOrReplace();
            Ingress orReplace1 = KubernetesUtils.client.extensions().ingresses().load(inputStream2).createOrReplace();
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
    @Override
    public List<Ingress> findAllIngresses(){

        List<Ingress> items = KubernetesUtils.client.extensions().ingresses().list().getItems();

        return items;

    }

    @Override
    public List<Ingress> findIngressesByNamespace(String namespace){

        List<Ingress> item = KubernetesUtils.client.extensions().ingresses().inNamespace(namespace).list().getItems();

        return item;
    }


    @Override
    public Ingress loadServiceFromYaml(String path) throws FileNotFoundException {

        InputStream yamlInputStream = byPath(path);

        Ingress ingress = KubernetesUtils.client.extensions().ingresses().load(yamlInputStream).get();

        return ingress;
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

        Boolean deleteIngress = KubernetesUtils.client.extensions().ingresses().inNamespace(namespace).withName(ingressName).delete();

        return deleteIngress;
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

        Ingress ingress = KubernetesUtils.client.extensions().ingresses().inNamespace(namespace).withName(name).get();

        return ingress;
    }

    @Override
    public String findIngressYamlByNameAndNamespace(String name, String namespace){

        Ingress ingress = KubernetesUtils.client.extensions().ingresses().inNamespace(namespace).withName(name).get();

        return Yaml.dump(ingress);

    }

}

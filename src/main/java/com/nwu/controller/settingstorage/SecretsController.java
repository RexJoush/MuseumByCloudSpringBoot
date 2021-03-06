package com.nwu.controller.settingstorage;

/**
 * @author Rex Joush
 * @time 2021.03.22
 */

import com.alibaba.fastjson.JSON;
import com.nwu.service.settingstorage.impl.SecretsServiceImpl;
import io.fabric8.kubernetes.api.model.Secret;
import io.kubernetes.client.openapi.ApiException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Secrets 的 controller 层
 */
@RestController
@RequestMapping("/secrets")
public class SecretsController {

    @Resource
    private SecretsServiceImpl secretsService;

    @RequestMapping("/getAllSecrets")
    public String findAllSecrets(String namespace) throws ApiException{

        List<Secret> secrets;

        if ("".equals(namespace)){
            secrets = secretsService.findAllSecrets();
        }else {
            secrets = secretsService.findSecretsByNamespace(namespace);
        }

        Map<String,Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "获取 Secret 列表成功");
        result.put("data", secrets);

        return JSON.toJSONString(result);

    }

    @RequestMapping("/getAllSecretsName")
    public String findAllSecretsName(String namespace) throws ApiException{

        List<Secret> secrets;

        if ("".equals(namespace)){
            secrets = secretsService.findAllSecrets();
        }else {
            secrets = secretsService.findSecretsByNamespace(namespace);
        }

        int amount = 0;
        boolean flag[] = new boolean[secrets.size()];
        List<List<String>> secretsInNamespace = new ArrayList<>();
        Map<String, Integer> index = new HashMap<>();
        for(Secret secret : secrets){
            if(index.get(secret.getMetadata().getNamespace()) == null){
                List<String> tmpSecretsList = new ArrayList<String>(Collections.singleton(secret.getMetadata().getName()));
                secretsInNamespace.add(tmpSecretsList);
                index.put(secret.getMetadata().getNamespace(), amount ++);
            }else{
                secretsInNamespace.get(index.get(secret.getMetadata().getNamespace())).add(secret.getMetadata().getName());
            }
        }
        Map<String, List<String>> secretsName = new HashMap<>();
        Set<String> strings = index.keySet();
        Iterator<String> iterator = strings.iterator();
        while(iterator.hasNext()){
            String tmpNamespace = iterator.next();
            secretsName.put(tmpNamespace, secretsInNamespace.get(index.get(tmpNamespace)));
        }


        Map<String,Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "获取 Secret 名字成功");
        result.put("data", secretsName);

        return JSON.toJSONString(result);

    }

    @RequestMapping("/getSecretsByNamespace")
    public String findSecretsByNamespace(String namespace) throws ApiException {

        List<Secret> v1SecretList = secretsService.findSecretsByNamespace(namespace);

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "获取 Secret 列表成功");
        result.put("data", v1SecretList);

        return JSON.toJSONString(result);

    }

    @RequestMapping("/delSecretByNameAndNamespace")
    public String deleteSecretByNameAndNamespace(String name, String namespace) throws ApiException{

        Boolean isDeleteSecrets = secretsService.deleteSecretByNameAndNamespace(name,namespace);

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "删除 Secret 成功");
        result.put("data", isDeleteSecrets);

        return JSON.toJSONString(result);
    }

    @RequestMapping("/loadSecret")
    public String loadSecretFromYaml(String path) throws FileNotFoundException {

        Secret secret = secretsService.loadSecretFromYaml(path);

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "加载 Secret 成功");
        result.put("data", secret);

        return JSON.toJSONString(result);
    }

    @RequestMapping("/createSecret")
    public String createSecretByYaml(String path) throws FileNotFoundException {

        Secret secretByYaml = secretsService.createSecretByYaml(path);

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "创建 Secret 成功");
        result.put("data", secretByYaml);

        return JSON.toJSONString(result);
    }

    @RequestMapping("/createOrReplaceSecret")
    public String createOrReplaceSecret(String path) throws FileNotFoundException {

        Secret secret = secretsService.createOrReplaceSecret(path);

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "创建或更新 Secret 成功");
        result.put("data", secret);

        return JSON.toJSONString(result);
    }

    @RequestMapping("/getSecretByNameAndNamespace")
    public String getSecretByNameAndNamespace(String name, String namespace){

        Secret secret = secretsService.getSecretByNameAndNamespace(name,namespace);

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "通过 name namespace 获取 Secret 成功");
        result.put("data", secret);

        return JSON.toJSONString(result);
    }

    @RequestMapping("/getSecretYamlByNameAndNamespace")
    public String getSecretYamlByNameAndNamespace(String name, String namespace) throws ApiException {

        String secretYaml = secretsService.getSecretYamlByNameAndNamespace(name, namespace);

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "获取 secret Yaml 成功");
        result.put("data", secretYaml);

        return JSON.toJSONString(result);
    }

}

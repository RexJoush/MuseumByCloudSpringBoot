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
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Secrets 的 controller 层
 */
@RestController
@RequestMapping("/secrets")
public class SecretsController {

    @Resource
    private SecretsServiceImpl secretsService;

    @RequestMapping("/getAllSecrets")
    public String findAllSecrets() throws ApiException{

        List<Secret> secrets = secretsService.findAllSecrets();

        Map<String,Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "获取 Secret 列表成功");
        result.put("data", secrets);

        return JSON.toJSONString(result);

    }

    @RequestMapping("getSecretsByNamespace")
    public String findSecretsByNamespace(String namespace) throws ApiException {

        List<Secret> v1SecretList = secretsService.findSecretsByNamespace(namespace);

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "获取 Secret 列表成功");
        result.put("data", v1SecretList);

        return JSON.toJSONString(result);

    }

    @RequestMapping("/deleteSecret")
    public String deleteSecretByNameAndNamespace(String name, String namespace) throws ApiException{

        Boolean isDeleteSecrets = secretsService.deleteSecretByNameAndNamespace(name,namespace);

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "删除 Secret 成功");
        result.put("data", isDeleteSecrets);

        return JSON.toJSONString(result);
    }

    @RequestMapping("/loadSecret")
    public String loadSecretFromYaml(InputStream yamlInputStream){

        Secret secret = secretsService.loadSecretFromYaml(yamlInputStream);

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "加载 Secret 成功");
        result.put("data", secret);

        return JSON.toJSONString(result);
    }

    @RequestMapping("/createSecret")
    public String createSecretByYaml(InputStream yamlInputStream){

        Secret secretByYaml = secretsService.createSecretByYaml(yamlInputStream);

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "创建 Secret 成功");
        result.put("data", secretByYaml);

        return JSON.toJSONString(result);
    }

    @RequestMapping("/createOrReplaceSecret")
    public String createOrReplaceSecret(InputStream yamlInputStream){

        Secret secret = secretsService.createOrReplaceSecret(yamlInputStream);

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "创建或更新 Secret 成功");
        result.put("data", secret);

        return JSON.toJSONString(result);
    }

}

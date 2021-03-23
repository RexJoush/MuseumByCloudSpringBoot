package com.nwu.controller.settingstorage;

/**
 * @author Rex Joush
 * @time 2021.03.22
 */

import com.alibaba.fastjson.JSON;
import com.google.protobuf.Api;
import com.nwu.service.settingstorage.impl.SecretsServiceImpl;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.Secret;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.proto.V1;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
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



}

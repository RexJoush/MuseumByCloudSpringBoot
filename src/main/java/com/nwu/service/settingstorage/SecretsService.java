package com.nwu.service.settingstorage;

/**
 * @author Rex Joush
 * @time 2021.03.22
 */

import io.fabric8.kubernetes.api.model.Secret;
import io.kubernetes.client.openapi.ApiException;

import java.util.List;

/**
 * Secrets 的 service 层接口
 */
public interface SecretsService {

    /**
     * 获取 Secret 列表
     * @return Secret 列表
     * @throws ApiException
     */
    List<Secret> findAllSecrets() throws ApiException;

    /**
     * 通过 namespace 获取 Secret 列表
     * @param namespace namespace 名称
     * @return Secret 列表
     */
    List<Secret> findSecretsByNamespace(String namespace);

}

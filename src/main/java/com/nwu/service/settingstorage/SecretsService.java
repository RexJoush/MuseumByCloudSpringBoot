package com.nwu.service.settingstorage;

/**
 * @author Rex Joush
 * @time 2021.03.22
 */

import io.fabric8.kubernetes.api.model.Secret;
import io.kubernetes.client.openapi.ApiException;

import java.io.InputStream;
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

    /**
     * 通过 name 和 namespace 删除 Secret
     * @param name Secret 名称
     * @param namespace Secret所在命名空间名称默认为“default”
     * @return 删除结果 bool型
     */
    Boolean deleteSecretByNameAndNamespace(String name,String namespace);

    /**
     * 通过加载 yaml 文件到 Secret 实例
     * @param yamlInputStream yaml文件输入流 InputStream
     * @return 加载的 Secret
     */
    Secret loadSecretFromYaml(InputStream yamlInputStream);

    /**
     * 通过 yaml 文件创建 secret
     * @param yamlInputStream yaml文件输入流 InputStream
     * @return 创建的 secret
     */
    Secret createSecretByYaml(InputStream yamlInputStream);

    /**
     * 通过yaml文件创建或更新 Secret
     * @param yamlInputStream yaml文件输入流 InputStream
     * @return 创建或更新的 Secret
     */
    Secret createOrReplaceSecret(InputStream yamlInputStream);
}

package com.nwu.service.settingstorage;

/**
 * @author Rex Joush
 * @time 2021.03.22
 */

import io.fabric8.kubernetes.api.model.Secret;
import io.kubernetes.client.openapi.ApiException;

import java.io.FileNotFoundException;
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
     * 通过加载yaml文件到 Secret 实例
     * @param path  yaml文件的路径
     * @return 加载的 Secret
     * @throws FileNotFoundException
     */
    Secret loadSecretFromYaml(String path) throws FileNotFoundException;

    /**
     * 通过 yaml 文件创建 secret
     * @param path yaml文件的路径
     * @return 创建的 secret
     * @throws FileNotFoundException
     */
    Secret createSecretByYaml(String path) throws FileNotFoundException;

    /**
     * 通过yaml文件创建或更新 Secret
     * @param path yaml文件的路径
     * @return 创建或更新的 Secret
     * @throws FileNotFoundException
     */
    Secret createOrReplaceSecret(String path) throws FileNotFoundException;

    /**
     * 通过Secret name和namespace获取Secret的信息
     * @param name Secret名称
     * @param namespace Secret命名空间
     * @return Secret信息
     */
    Secret getSecretByNameAndNamespace(String name, String namespace);

    /**
     * 通过名字和命名空间查找 Secret 的 yaml 文件
     * @param name Secret 名字
     * @param namespace Secret 命名空间
     * @return 查找到的 Secret 的 yaml 格式文件
     */
    String getSecretYamlByNameAndNamespace(String name, String namespace) throws ApiException;;
}

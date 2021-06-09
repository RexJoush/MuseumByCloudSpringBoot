package com.nwu.util;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DockerClientConfig;

/**
 * @author Rex Joush
 * @time 2021.05.11 19:58
 */

public class DockerUtils {

    public static DockerClient docker;

    public static String baseUrl = "192.168.29.147:2376";

    public static String httpUrl = "http://" + baseUrl;

    static {

        DockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder()
                .withDockerHost("tcp://" + baseUrl)
                .withDockerTlsVerify(false)
                .withApiVersion("1.41")
                .build();

        docker = DockerClientBuilder.getInstance(config).build();
    }

}

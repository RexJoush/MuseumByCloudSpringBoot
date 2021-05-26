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

    static {

//        String url = "tcp://172.18.7.15:2376";
        String url = "tcp://192.168.29.145:2376";

        DockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder()
                .withDockerHost(url)
                .withDockerTlsVerify(false)
                .withApiVersion("1.41")
                .build();

        docker = DockerClientBuilder.getInstance(config).build();
    }


}

package com.nwu.util;

import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.util.ClientBuilder;
import io.kubernetes.client.util.credentials.AccessTokenAuthentication;

/**
 * @author Rex Joush
 * @time 2021.03.22
 */

/**
 * 关于连接云平台的配置信息
 */
public class KubernetesConfig {

    private static Config config;
    static {
        // kubernetes 连接 token
        String token = "eyJhbGciOiJSUzI1NiIsImtpZCI6IlJTdmpKMTByMndWVU91V0xKYkpjY2FuV0tBbk94c0xQVkhaUlBtckZOdEUifQ.eyJpc3MiOiJrdWJlcm5ldGVzL3NlcnZpY2VhY2NvdW50Iiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9uYW1lc3BhY2UiOiJrdWJlcm5ldGVzLWRhc2hib2FyZCIsImt1YmVybmV0ZXMuaW8vc2VydmljZWFjY291bnQvc2VjcmV0Lm5hbWUiOiJhZG1pbi11c2VyLXRva2VuLTI0bnI0Iiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9zZXJ2aWNlLWFjY291bnQubmFtZSI6ImFkbWluLXVzZXIiLCJrdWJlcm5ldGVzLmlvL3NlcnZpY2VhY2NvdW50L3NlcnZpY2UtYWNjb3VudC51aWQiOiI2MzA0ZTg0Ny04ZTBmLTRlMjctOTBlMi01MjkyOGEzODE5NzkiLCJzdWIiOiJzeXN0ZW06c2VydmljZWFjY291bnQ6a3ViZXJuZXRlcy1kYXNoYm9hcmQ6YWRtaW4tdXNlciJ9.dbxJZVivp1Yxnk1X5pwuvWBvlZtpZvV6G5DxMX25lJtLzfnIxBlvn3sbeVv_4LvUDh21cmapLB6zcQ_VJXdkUM-EdGmZQNwLu8UrTLWuyELZ947TMRnR0uFJx-cnH9Ir7eHMGpL6wmiyVXe6DfJq_0AfOv9XYZssKi1aaKQPu-YYnQC6hdIG_KpjObaK8WSXjhW5UrNAvMNP0mdXNE8HhqgzcVnza3bWl-gfGg69tbBzUKL9ZqY40UF9w3vdhufBPV4f4J2tQfBbMk9vrqSzzAOADbYG1nojz3s7SeCO0NJwSQVyCOoqtHkVL48AdnkguQcJwSRtu93H2VvM8YqWRA";

        // kubernetes 连接地址
        String kubernetesUrl = "https://172.18.7.16:6443";

        config = new ConfigBuilder().withMasterUrl(kubernetesUrl).withTrustCerts(true).withOauthToken(token).build();

    }

    // 创建 Kubernetes 客户端
    public static KubernetesClient client = new DefaultKubernetesClient(config);

}

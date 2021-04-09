package com.nwu.util;

import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;

/**
 * @author Rex Joush
 * @time 2021.03.22
 */

/**
 * 关于连接云平台的配置信息
 */
public class KubernetesUtils {

    private static Config config;

    static {
        // kubernetes 连接 token
        String token = "eyJhbGciOiJSUzI1NiIsImtpZCI6IlhOc2N4Q000WEZoZHJGanNfWkg1UXNhRkJfNHFESk1JenB5T1ptNnBUMTAifQ.eyJpc3MiOiJrdWJlcm5ldGVzL3NlcnZpY2VhY2NvdW50Iiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9uYW1lc3BhY2UiOiJrdWJlcm5ldGVzLWRhc2hib2FyZCIsImt1YmVybmV0ZXMuaW8vc2VydmljZWFjY291bnQvc2VjcmV0Lm5hbWUiOiJhZG1pbi11c2VyLXRva2VuLWJja3NwIiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9zZXJ2aWNlLWFjY291bnQubmFtZSI6ImFkbWluLXVzZXIiLCJrdWJlcm5ldGVzLmlvL3NlcnZpY2VhY2NvdW50L3NlcnZpY2UtYWNjb3VudC51aWQiOiJkYjE2NTMyZi00OWIxLTQ4ZDEtYWI5Ni1kOGM4NWI1MGFhMWUiLCJzdWIiOiJzeXN0ZW06c2VydmljZWFjY291bnQ6a3ViZXJuZXRlcy1kYXNoYm9hcmQ6YWRtaW4tdXNlciJ9.rJ6HLq7BCFGn-aZ6lu4CYqVKksPHJSwwI04Hnlt8yhM3w0ReOCRp4HD-K1zkfih7hcDedGcPVQyjS9B57YCHJkgIAcYO3UghiHo-hyOycbPpBukxpiuao5LuZJKthMlVrgf0wD9utiYBUek_ngB0mJNX4GrtZ3E5gtBRYFWdfA_9j60zxeEcYcHH4spd5KXFOV0lRUg_McVZPOKsFJCRRWbGbJWkGOAr8TlbAtMEQA03dz6Kw6zCR6al2dfLufXoavIq2RKvg8_eX8I6tHQfiExMC5uafeVBLXTrMYMKzCom1A3kVMIaxG3YqS91gg1uCaqJdRK3U91xECaQjiPGdA";
        // kubernetes 连接地址
        String kubernetesUrl = "https://172.18.7.23:6443";

        config = new ConfigBuilder().withMasterUrl(kubernetesUrl).withTrustCerts(true).withOauthToken(token).build();

    }

    // 创建 Kubernetes 客户端
    public static KubernetesClient client = new DefaultKubernetesClient(config);

    // 增删资源的 yaml 文件临时路径
    public static String path = "D:\\Files\\a.yaml";

}

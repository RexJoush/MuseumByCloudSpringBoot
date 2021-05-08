package com.nwu.util;

import io.fabric8.kubernetes.client.*;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.*;
import io.kubernetes.client.proto.Resource;

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
//        String token = "eyJhbGciOiJSUzI1NiIsImtpZCI6ImlmY1hHeVB2dVJ6M2hKdDFyTzAyWDJZMTlEUFhZQ29VMlVRMVZ2VmZXT00ifQ.eyJpc3MiOiJrdWJlcm5ldGVzL3NlcnZpY2VhY2NvdW50Iiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9uYW1lc3BhY2UiOiJrdWJlcm5ldGVzLWRhc2hib2FyZCIsImt1YmVybmV0ZXMuaW8vc2VydmljZWFjY291bnQvc2VjcmV0Lm5hbWUiOiJhZG1pbi11c2VyLXRva2VuLThya3NiIiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9zZXJ2aWNlLWFjY291bnQubmFtZSI6ImFkbWluLXVzZXIiLCJrdWJlcm5ldGVzLmlvL3NlcnZpY2VhY2NvdW50L3NlcnZpY2UtYWNjb3VudC51aWQiOiIwMDMzZjg1Yy1lODllLTQxNjAtYmYwNC02N2E4MmI3ZGZjMjIiLCJzdWIiOiJzeXN0ZW06c2VydmljZWFjY291bnQ6a3ViZXJuZXRlcy1kYXNoYm9hcmQ6YWRtaW4tdXNlciJ9.l47AJ5ClvvGz8xgRzT58LzrR68IsDpucyX9WhwdTtmtJ0sVHZ7s2XPdp-m6ATOx6DM3M6zjtiJFb-SlIMeX8QjNv4KL-if6KPi0y4swT7heTZMLEvj7yK-NhNIxTCWtSv6GuUgm1J4tFlnKAMr-MIgj4H4Bj_AKoUBwYb7hFsHhyFg5gFk0bLVHNgl6JRmCDZnGL-YHG60jVaSH6RCkgJsrwLKnZ4iRFNNh_HT_w9TpDm0gvlo7WLK7-H9i4FBYWSwiwluqsMsQQNNRIbHX6K8pE2rqI4VcY4JST5Xb6ERZNjOJgO7I9qEIICd5gjCtsJ9F0t0TwJYTlVJa6OkaYHQ";

        // kubernetes 连接地址
        String kubernetesUrl = "https://172.18.7.23:6443";
//        String kubernetesUrl = "https://172.18.7.25:7443";

        config = new ConfigBuilder().withMasterUrl(kubernetesUrl).withTrustCerts(true).withOauthToken(token).build();

        ApiClient apiClient = io.kubernetes.client.util.Config.fromToken(kubernetesUrl, token, false);
        Configuration.setDefaultApiClient(apiClient);
    }

    // 创建 Kubernetes Official RbacAuthorizationV1Api 客户端
    public static RbacAuthorizationV1Api rbacAuthorizationV1Api = new RbacAuthorizationV1Api();

//    public static ApiextensionsV1Api apiextensionsV1Api= new ApiextensionsV1Api().getAPIResources().re;

    // 创建 Kubernetes Official CoreV1Api 客户端
    public static CoreV1Api coreV1Api = new CoreV1Api();

    // 创建 Kubernetes Official ExtensionsV1Beta1Api 客户端
    public static ExtensionsV1beta1Api extensionsV1beta1Api = new ExtensionsV1beta1Api();
    // 创建 Kubernetes Official BatchApi 客户端
    public static BatchV1Api batchV1Api = new BatchV1Api();

    // 创建 Fabric8 Kubernetes客户端
    public static KubernetesClient client = new DefaultKubernetesClient(config);

    // 增删资源的 yaml 文件临时路径
    public static String path = "D:\\Files\\a.yaml";


}

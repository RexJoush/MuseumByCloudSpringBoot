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

    private static final Config config;

    static {
        // kubernetes 连接 token

        // 新平台
//        String token = "eyJhbGciOiJSUzI1NiIsImtpZCI6ImlmY1hHeVB2dVJ6M2hKdDFyTzAyWDJZMTlEUFhZQ29VMlVRMVZ2VmZXT00ifQ.eyJpc3MiOiJrdWJlcm5ldGVzL3NlcnZpY2VhY2NvdW50Iiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9uYW1lc3BhY2UiOiJrdWJlcm5ldGVzLWRhc2hib2FyZCIsImt1YmVybmV0ZXMuaW8vc2VydmljZWFjY291bnQvc2VjcmV0Lm5hbWUiOiJhZG1pbi11c2VyLXRva2VuLXdtcGNwIiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9zZXJ2aWNlLWFjY291bnQubmFtZSI6ImFkbWluLXVzZXIiLCJrdWJlcm5ldGVzLmlvL3NlcnZpY2VhY2NvdW50L3NlcnZpY2UtYWNjb3VudC51aWQiOiJlN2QxMTk2Mi1iNjhhLTQ1MDQtOGVkZC0yZWI0OGUwZjAzYzEiLCJzdWIiOiJzeXN0ZW06c2VydmljZWFjY291bnQ6a3ViZXJuZXRlcy1kYXNoYm9hcmQ6YWRtaW4tdXNlciJ9.U09IXkrWn5LhidpYzZCPP0-iC_TwgCGZPG63Pccu7Hwzll_0KmSCRfIlw-7s-Q2zbtEnI-I8MLn_tqDTYfinvflz24NXBgaU9UvYt-NIKjuXGhPQgxN8KO-Uhu5QX2oEypDQY8K3TJvVGoS3WSOufLvVqqz5ozPCqDAhex0645CQ97GbuHSh0DT_q0Atnrvi3x3tuoUSfvJ-_8i6wzE39FZaYWiSJya4Y5Kq8ZnG9v_keEe3x2eog2qkLrDODtwQmg14kapLVXXQAMb92RqHpIhh1nnO4BdjVKoYzqPpnMnCZR7c1Pc2QOzFjoXScFPHrbe_qbdBDp93j7i14rfjgg";

        // 旧平台
        String token = "eyJhbGciOiJSUzI1NiIsImtpZCI6IlhOc2N4Q000WEZoZHJGanNfWkg1UXNhRkJfNHFESk1JenB5T1ptNnBUMTAifQ.eyJpc3MiOiJrdWJlcm5ldGVzL3NlcnZpY2VhY2NvdW50Iiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9uYW1lc3BhY2UiOiJrdWJlcm5ldGVzLWRhc2hib2FyZCIsImt1YmVybmV0ZXMuaW8vc2VydmljZWFjY291bnQvc2VjcmV0Lm5hbWUiOiJhZG1pbi11c2VyLXRva2VuLTRwOGxsIiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9zZXJ2aWNlLWFjY291bnQubmFtZSI6ImFkbWluLXVzZXIiLCJrdWJlcm5ldGVzLmlvL3NlcnZpY2VhY2NvdW50L3NlcnZpY2UtYWNjb3VudC51aWQiOiIzMzcyODlhZi05NzEzLTRlOTgtOTMzZi00OTFlNDEzNTlhMmUiLCJzdWIiOiJzeXN0ZW06c2VydmljZWFjY291bnQ6a3ViZXJuZXRlcy1kYXNoYm9hcmQ6YWRtaW4tdXNlciJ9.eiwDy2oGCHIGRxuUksbOQd3EdAnPC2y1jDl9w4nMA4bUufWOsokW2usYdrKR-UFmUManDiVyXb6Xi8JhzSOBQSbDQ3F3EtfsvUZ6DH7CSeQ1vGYNEHRSI5FeVodVsMXJCGeuUQjc-zZFfQpiXlpbl1DDW2lUScaviKxJPAMxbzf9JZbPw4RNRFECuWSTsWyPHqZ81GO4BU07aFIkJtnYGEPe-Ob_rl7nClJIC-id98vPotqYbXv2oDi54pg91aeyLFjOb1GDiQndQWdBS-E6v4AzN0zYBXBFGp0OyT6My-dd-CLAX--KHru6haINyVyQ2w0KMwPdsmIqXPvUrfVZoA";

        // kubernetes 连接地址
        // 新平台
//        String kubernetesUrl = "https://172.18.7.25:7443";

        // 旧平台
        String kubernetesUrl = "https://172.18.7.23:6443";


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

    public static AppsV1Api appsV1Api = new AppsV1Api();

    // 创建 Fabric8 Kubernetes客户端
    public static KubernetesClient client = new DefaultKubernetesClient(config);

    // 增删资源的 yaml 文件临时路径
    public static String path = "D:\\Files\\a.yaml";


}

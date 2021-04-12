package com.nwu.util.format;


//import io.fabric8.kubernetes.api.model.metrics.v1beta1.


///**
// * 格式化 configMap 列表的工具函数
// */
public class ConfigMapFormat {

//    DecimalFormat df = new DecimalFormat("#.##");
//
//    /**
//     * 封装获取的 configMap 列表，包含利用率信息和 configMap 信息
//     * @return 封装好的列表
//     */
//    public static List<ConfigMapDefinition> formatConfigMapList(List<ConfigMap> items) {
//
//        // 返回结果列表
//        List<ConfigMapDefinition> result = new ArrayList<>();
//
//        List<ConfigMapMetrics> tops = KubernetesUtils.client.configMaps().me
//
//        Map<String, Usage> usage = new HashMap<>();
//
//        for (PodMetrics top : tops){
//            if (top.getContainers().size() > 0) {
//                usage.put(top.getMetadata().getName() + top.getMetadata().getNamespace(),
//                        new Usage(top.getContainers().get(0).getUsage()
//                                .get("cpu").getAmount(), top.getContainers().get(0).getUsage()
//                                .get("memory").getAmount()));
//            }
//        }
//
//        // 封装 pod 列表
//        for (Pod item : items) {
//
//            PodDefinition pod = new PodDefinition();
//
//            // 设置名称和命名空间
//            pod.setName(item.getMetadata().getName());
//            pod.setNamespace(item.getMetadata().getNamespace());
//
//            // 设置状态
//            pod.setPhase(item.getStatus().getPhase());
//
//            // 设置重启次数
//            if (item.getStatus().getContainerStatuses().size() > 0) {
//                pod.setRestartCount(item.getStatus().getContainerStatuses().get(0).getRestartCount());
//            } else {
//                pod.setRestartCount(0);
//            }
//
//            // 设置内存和 cpu 利用率
//            String key = item.getMetadata().getName()+item.getMetadata().getNamespace();
//            if (usage.containsKey(key)) {
//                pod.setCpuUsage(usage.get(key).getCpu());
//                pod.setMemoryUsage(usage.get(key).getMemory());
//            } else {
//                pod.setCpuUsage("-1");
//                pod.setMemoryUsage("-1");
//            }
//
//            // 设置主机名和 ip 信息
//            pod.setNodeName(item.getSpec().getNodeName());
//            pod.setPodIP(item.getStatus().getPodIP());
//
//            result.add(pod);
//
//        }
//
//        return result;
//    }



}

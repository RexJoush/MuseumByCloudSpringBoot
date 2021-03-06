package com.nwu.service.cluster.impl;

/**
 * @author Rex Joush
 * @time 2021.04.13
 */

import com.nwu.entity.cluster.graph.*;
import com.nwu.service.cluster.ClusterService;
import com.nwu.util.KubernetesUtils;
import io.fabric8.kubernetes.api.model.Node;
import io.fabric8.kubernetes.api.model.Pod;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


/**
 * Cluster Service 实现类
 */
@Service
public class ClusterServiceImpl implements ClusterService {

    /**
     * 初始化图
     * master node 节点图大小为 66
     * node, edge node 为 50
     * pod 为 33
     * @return 图的数据
     */
    @Override
    public ClusterGraph initClusterGraph() {
        // 初始化
        ClusterGraph graph = new ClusterGraph();
        // 图节点列表
        List<GraphNode> nodes = new ArrayList<>();
        // 关系列表
        List<GraphLink> links = new ArrayList<>();

        // 类别列表
        List<GraphCategory> categories = new ArrayList<>();

        categories.add(new GraphCategory(new ItemStyle("#da723c"),"主节点"));         // 0
        categories.add(new GraphCategory(new ItemStyle("#b68973"),"节点"));           // 1
        categories.add(new GraphCategory(new ItemStyle("#32CD32"),"容器（运行中）"));  // 2
        categories.add(new GraphCategory(new ItemStyle("#F56C6C"),"容器（失败）"));    // 3
        categories.add(new GraphCategory(new ItemStyle("#409EFF"),"容器（成功）"));    // 4
        int defaultValue = 0;

        int index = 0; // 所有图节点的 id 序列

        // 获取所有节点
        // List<Node> items = KubernetesUtils.client.nodes().list().getItems();
        List<Node> items = KubernetesUtils.client.nodes().withLabelIn("node-type", "normal-node", "master-node").list().getItems();


        // 获取主节点坐标
        List<Coordinate> coordinates = getNodeCoordinate(items.size());

        for (int i = 0; i < items.size(); i++) {
            GraphNode node = new GraphNode();
            String name = items.get(i).getMetadata().getName();
            node.setId(index);
            node.setValue(name);

            if ("master-node".equals(items.get(i).getMetadata().getLabels().get("node-type"))) {
                node.setSymbolSize("40");
                node.setName("Master Node");
                node.setCategory(0);
            } else {
                node.setSymbolSize("30");
                node.setName("Node");
                node.setCategory(1);
            }

            node.setX(coordinates.get(i).getX());
            node.setY(coordinates.get(i).getY());

            // 添加主节点
            nodes.add(node);
            // 记录当前 node 的 id 值
            int anchorPoint = index;
            index++;

            // 获取当前节点的 pod 列表
            List<Pod> pods = KubernetesUtils.client.pods().inAnyNamespace().withField("spec.nodeName", name).list().getItems();

            for (Pod pod : pods) {
                // 新建图节点
                GraphNode subNode = new GraphNode();
                // 获取 pod 名称
                String podName = pod.getMetadata().getName();
                // 获取 pod 命名空间
                String podNamespace = pod.getMetadata().getNamespace();

                // 设置图 pod 节点 id
                subNode.setId(index);
                // 设置图 pod 节点名称
                subNode.setName("Pod");

                // 设置 值
                subNode.setValue(podName);
                subNode.setNamespace(podNamespace);

                // 设置样式
                subNode.setSymbolSize("15");
                // 设置坐标
                Coordinate subNodeCoordinate = getSubNodeCoordinate(coordinates.get(i));
                subNode.setX(subNodeCoordinate.getX());
                subNode.setY(subNodeCoordinate.getY());

                // 设置类别
                switch (pod.getStatus().getPhase()){
                    case "Running" :
                        subNode.setCategory(2);
                        break;
                    case "Pending" :
                        subNode.setCategory(3);
                        break;
                    case "Succeeded" :
                        subNode.setCategory(4);
                        break;
                    default:
                        defaultValue++;
                        subNode.setCategory(5);
                }

                // 新建连接线
                GraphLink link = new GraphLink();
                link.setSource(index);
                link.setTarget(anchorPoint);

                // 加入连接线集合
                links.add(link);
                // 加入节点集合
                nodes.add(subNode);
                index++;
            }

        }

        // 如果有例外的类别，则添加此类别
        if (defaultValue > 0){
            categories.add(new GraphCategory(new ItemStyle("#03506f"), "容器"));           // 5
        }

        graph.setNodes(nodes);
        graph.setLinks(links);
        graph.setCategories(categories);

        return graph;
    }

    /**
     * 根据父节点的极坐标角度范围获取子节点的直角坐标值
     * @param coordinate 父节点
     * @return 子节点直角坐标
     */
    private Coordinate getSubNodeCoordinate(Coordinate coordinate) {

        // 获取子节点的坐标范围上下限
        double low = coordinate.getSpanLow();
        double high = coordinate.getSpanHigh();

        double rho = Math.round(Math.random() * 1000 + 200);
        double theta = Math.random() * (high - low) + low;

        return new Coordinate(rho * Math.cos(theta), rho * Math.sin(theta), 0.0, 0.0);
    }


    /**
     * 获取主节点的坐标
     * @param size 主节点的个数，根据个数，通过极坐标进行划分
     * @return 主节点的坐标列表
     */
    private List<Coordinate> getNodeCoordinate(int size) {

        List<Coordinate> coordinates = new LinkedList<>();

        // 获取每个节点之间的角度
        double span = 2 * Math.PI / size;
        //
        for (int i = 0; i < size; i++) {
            Coordinate coordinate = new Coordinate();

            // ρ 值
            double rho = 100;

            // θ 值，通过旋转 span 个角度获取每个角度值
            double theta = i * span;

            // 极直转换
            coordinate.setX(rho * Math.cos(theta));
            coordinate.setY(rho * Math.sin(theta));

            // 设置子节点的随机范围
            coordinate.setSpanLow(theta - span / 2);
            coordinate.setSpanHigh(theta + span / 2);

            coordinates.add(coordinate);
        }
        return coordinates;
    }

}

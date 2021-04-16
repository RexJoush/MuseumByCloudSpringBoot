package com.nwu.service.cluster.impl;

/**
 * @author Rex Joush
 * @time 2021.04.13
 */

import com.nwu.entity.cluster.ClusterGraph;
import com.nwu.entity.cluster.GraphCategory;
import com.nwu.entity.cluster.GraphLink;
import com.nwu.entity.cluster.GraphNode;
import com.nwu.service.cluster.ClusterService;
import com.nwu.util.KubernetesUtils;
import io.fabric8.kubernetes.api.model.Node;
import io.fabric8.kubernetes.api.model.Pod;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


/**
 * Cluster Service 实现类
 */
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

        int index = 0; // 所有图节点的 id 序列

        // 获取所有节点
        List<Node> items = KubernetesUtils.client.nodes().list().getItems();

        // 获取主节点坐标
        List<Coordinate> coordinates = getNodeCoordinate(items.size());


        // 遍历 node，添加 node 的图节点
        for (Node item : items) {
            // 新建一个 图节点，node 节点
            GraphNode node = new GraphNode();
            String name = item.getMetadata().getName();
            node.setId(index);
            node.setName(name);
            if (name.contains("master")){
                node.setSymbolSize("66");
                node.setValue("Master Node");
            } else if (name.contains("edge")){
                node.setSymbolSize("50");
                node.setValue("Node");
            } else {
                node.setSymbolSize("50");
                node.setValue("Edge Node");
            }
            // 判断节点该往那个方向发展
            switch (index % 4){
                // 第一象限，右下角
                case 0:
                    node.setX(index);
                    node.setY(index);
                    break;
                // 第二象限，左下角
                case 1:
                    node.setX(index * 10);
                    node.setY(index * -10);
                    break;
                // 第三象限，左上角
                case 2:
                    node.setX(index * -10);
                    node.setY(index * -10);
                    break;
                // 第四象限，右上角
                case 3:
                    node.setX(index * -10);
                    node.setY(index * 10);
                    break;
                default:
                    node.setX(index * 20);
                    node.setY(index * 20);
                    break;
            }
            // 添加节点进入图节点中
            nodes.add(node);

            // 记录 pod 节点的坐标值
            int subIndex = 0;
            // 获取当前节点的 pod 列表
            List<Pod> pods = KubernetesUtils.client.pods().inAnyNamespace().withField("spec.nodeName", name).list().getItems();

            // 记录当前节点的基点
            int anchorPoint = index;

            // id 值自增
            index++;

            // 遍历 pod 列表
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
                subNode.setName(podName + "," + podNamespace);

                // 判断节点该往那个方向发展
                switch (anchorPoint % 4){
                    // 第一象限，右下角
                    case 0:
                        node.setX(anchorPoint);
                        break;
                    // 第二象限，左下角
                    case 1:
                        node.setX(index + 1);
                        node.setY(index - 1);
                        break;
                    // 第三象限，左上角
                    case 2:
                        node.setX(index - 1);
                        node.setY(index - 1);
                        break;
                    // 第四象限，右上角
                    case 3:
                        node.setX(index - 1);
                        node.setY(index + 1);
                        break;
                    default:
                        node.setX(index + 2);
                        node.setY(index + 2);
                        break;
                }

            }
        }

        return graph;
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
            double rho = 10;

            // θ 值，通过旋转 span 个角度获取每个角度值
            double theta = i * span;

            // 极直转换
            coordinate.setX(rho * Math.cos(theta));
            coordinate.setY(rho * Math.sin(theta));
            coordinates.add(coordinate);
        }
        return coordinates;
    }




    public int getRandom(){
        return (int) Math.round(Math.random() * 100);
    }
}

class Coordinate {
    double x;
    double y;

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public Coordinate() {
    }

    public Coordinate(double x, double y) {
        this.x = x;
        this.y = y;
    }
}

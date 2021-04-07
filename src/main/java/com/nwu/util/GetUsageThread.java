package com.nwu.util;

import com.nwu.service.cluster.impl.NodesServiceImpl;
import com.nwu.service.workload.impl.PodsServiceImpl;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author Rex Joush
 * @time 2021.03.29
 */

/**
 * 获取资源的线程，通过实现 CommandLineRunner 接口来实现启动后执行该任务
 */
@Service
public class GetUsageThread implements CommandLineRunner {

    @Resource
    private NodesServiceImpl nodesService;

    @Resource
    private PodsServiceImpl podsService;

    @Override
    public void run(String... args) throws Exception {
        nodesService.saveNodeUsage(); // 保存 node 利用率信息
        podsService.savePodUsage(); // 保存 pod 利用率信息

        nodesService.deleteNodeUsage(); // 删除 node 利用率信息
        podsService.deletePodUsage(); // 删除 pod 的利用率信息
    }
}

package com.nwu.service.impl;

import com.nwu.service.LoadForecastingService;
import com.nwu.service.workload.DeploymentsService;
import io.kubernetes.client.openapi.ApiException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Rex Joush
 * @time 2021.03.22
 */

/**
 * 负载预测的 service 层实现类
 */
@Service
public class LoadForecastingServiceImpl implements LoadForecastingService {

    @Resource
    private DeploymentsService deploymentsService;

    public String expandShrinkPod(String name, String namespace, int replicas){

        try {
            deploymentsService.setReplicas(name, namespace, replicas);
            return "success";
        }
        catch (Exception e){
            return "error";
        }

    }

}

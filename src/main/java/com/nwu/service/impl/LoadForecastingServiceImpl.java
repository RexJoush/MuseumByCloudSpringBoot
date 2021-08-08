package com.nwu.service.impl;

import com.nwu.service.LoadForecastingService;
import com.nwu.service.workload.DeploymentsService;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.kubernetes.client.openapi.ApiException;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author Rex Joush
 * @time 2021.03.22
 */

/**
 * 负载预测的 service 层实现类
 */
@Service
public class LoadForecastingServiceImpl implements LoadForecastingService {

    private Pair<Integer, Boolean> pair;

    @Resource
    private DeploymentsService deploymentsService;

//    public String expandShrinkPod(String name, String namespace, int replicas){
//
//        try {
//            deploymentsService.setReplicas(name, namespace, replicas);
//            return "success";
//        }
//        catch (Exception e){
//            return "error";
//        }
//
//    }



    //定时执行

    public  Pair<Integer, Boolean> timer(Date date,String name,String namespace,String replica) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                DeploymentsService deploymentsService = null;
                pair=deploymentsService.setReplicas(name, namespace, Integer.parseInt(replica));
            }
        }, date);
        return pair;
    }
}

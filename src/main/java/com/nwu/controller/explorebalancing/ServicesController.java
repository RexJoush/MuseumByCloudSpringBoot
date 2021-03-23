package com.nwu.controller.explorebalancing;

/**
 * @author Rex Joush
 * @time 2021.03.22
 */

import com.alibaba.fastjson.JSON;
import com.nwu.service.explorebalancing.impl.ServicesServiceImpl;
import com.nwu.service.workload.impl.PodsServiceImpl;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.Service;
import io.kubernetes.client.openapi.ApiException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Services 的 controller 层
 */
@RestController
@RequestMapping("/services")
public class ServicesController {
    public static void main(String[] args) {
        System.out.println(new ServicesServiceImpl().findAllServices());
    }

    @Resource
    private ServicesServiceImpl serviceService;

    @RequestMapping("getAllServices")
    public String findAllServices() throws ApiException {

        List<Service> services = serviceService.findAllServices();

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "获取 Service 列表成功");
        result.put("data",services);

        return JSON.toJSONString(result);

    }


}

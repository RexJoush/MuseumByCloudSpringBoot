package com.nwu.controller.settingstorage;

/**
 * @author Rex Joush
 * @time 2021.03.22
 */

import com.alibaba.fastjson.JSON;
import com.nwu.service.settingstorage.impl.PersistentVolumeClaimsServiceImpl;
import io.fabric8.kubernetes.api.model.PersistentVolumeClaim;
import io.fabric8.kubernetes.api.model.Pod;
import io.kubernetes.client.openapi.ApiException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Persistent Volume Claims 的 controller 层
 */
@RestController
@RequestMapping("/persistentVolumeClaims")
public class PersistentVolumeClaimsController {

    @Resource
    private PersistentVolumeClaimsServiceImpl persistentVolumeClaimsService;

    @RequestMapping("/getAllPersistentVolumeClaims")
    public String findAllPersistentVolumeClaims() throws ApiException{

        List<PersistentVolumeClaim> persistentVolumeClaims = persistentVolumeClaimsService.findAllPersistentVolumeClaims();

        Map<String,Object> result = new HashMap<>();

        result.put("code",1200);
        result.put("message", "获取 PersistentVolumeClaim 列表成功");
        result.put("data", persistentVolumeClaims);

        return JSON.toJSONString(result);
    }

    @RequestMapping("getPersistentVolumeClaimsByNamespace")
    public String findPersistentVolumeClaimsByNamespace(String namespace) throws ApiException {

        List<PersistentVolumeClaim> v1PersistentVolumeClaimList = persistentVolumeClaimsService.findPersistentVolumeClaimsByNamespace(namespace);

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "获取 PersistentVolumeClaim 列表成功");
        result.put("data", v1PersistentVolumeClaimList);

        return JSON.toJSONString(result);

    }



}

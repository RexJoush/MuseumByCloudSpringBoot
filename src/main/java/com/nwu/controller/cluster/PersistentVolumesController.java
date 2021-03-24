package com.nwu.controller.cluster;

import com.alibaba.fastjson.JSON;
import com.nwu.service.cluster.impl.PersistentVolumesServiceImpl;
import io.fabric8.kubernetes.api.model.Namespace;
import io.fabric8.kubernetes.api.model.PersistentVolume;
import io.kubernetes.client.openapi.ApiException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zqy
 * @time 2021.03.24
 */

@RestController
@RequestMapping("/persistentVolume")
public class PersistentVolumesController {

    @Resource
    private PersistentVolumesServiceImpl persistentVolumesService;

    @RequestMapping("/getAllPersistentVolumes")
    public String getAllPersistentVolumes() throws ApiException {

        List<PersistentVolume> persistentVolumesList = persistentVolumesService.getAllPersistentVolumes();

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "获取 PersistentVolume 列表成功");
        result.put("data", persistentVolumesList);

        return JSON.toJSONString(result);
    }
}

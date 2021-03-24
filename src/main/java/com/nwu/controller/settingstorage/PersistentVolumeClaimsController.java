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
import java.io.FileNotFoundException;
import java.io.InputStream;
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

    @RequestMapping("/getAllPVC")
    public String findAllPVC() throws ApiException{

        List<PersistentVolumeClaim> persistentVolumeClaims = persistentVolumeClaimsService.findAllPVC();

        Map<String,Object> result = new HashMap<>();

        result.put("code",1200);
        result.put("message", "获取 PersistentVolumeClaim 列表成功");
        result.put("data", persistentVolumeClaims);

        return JSON.toJSONString(result);
    }

    @RequestMapping("getPVCByNamespace")
    public String findPVCByNamespace(String namespace) throws ApiException {

        List<PersistentVolumeClaim> v1PVC = persistentVolumeClaimsService.findPVCByNamespace(namespace);

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "获取 PersistentVolumeClaim 列表成功");
        result.put("data", v1PVC);

        return JSON.toJSONString(result);

    }

    @RequestMapping("/deletePVC")
    public String deletePVCByNameAndNamespace(String name, String namespace){
        Boolean aBoolean = persistentVolumeClaimsService.deletePVCByNameAndNamespace(name, namespace);

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "删除 PersistentVolumeClaim 成功");
        result.put("data", aBoolean);

        return JSON.toJSONString(result);
    }

    @RequestMapping("/loadPVC")
    public String loadPVCFromYaml(String path) throws FileNotFoundException {

        PersistentVolumeClaim persistentVolumeClaim = persistentVolumeClaimsService.loadPVCFromYaml(path);

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "加载 PersistentVolumeClaim 成功");
        result.put("data", persistentVolumeClaim);

        return JSON.toJSONString(result);
    }

    @RequestMapping("/createPVC")
    public String createPVCByYaml(String path) throws FileNotFoundException {

        PersistentVolumeClaim pvcByYaml = persistentVolumeClaimsService.createPVCByYaml(path);

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "创建 PersistentVolumeClaim 成功");
        result.put("data", pvcByYaml);

        return JSON.toJSONString(result);
    }

    @RequestMapping("/createOrReplacePVC")
    public String createOrReplacePVC(String path) throws FileNotFoundException{
        PersistentVolumeClaim pvc = persistentVolumeClaimsService.createOrReplacePVC(path);

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "创建或更新 PersistentVolumeClaim 成功");
        result.put("data", pvc);

        return JSON.toJSONString(result);
    }
}

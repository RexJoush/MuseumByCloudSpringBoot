package com.nwu.controller.cluster;

import com.alibaba.fastjson.JSON;
import com.nwu.service.cluster.impl.StorageClassesServiceImpl;
import io.fabric8.kubernetes.api.model.storage.StorageClass;
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
@RequestMapping("/storageClasses")
public class StorageClassesController {

    @Resource
    private StorageClassesServiceImpl storageClassesService;

    @RequestMapping("/getAllStorageClasses")
    public String getAllStorageClasses() throws ApiException {

        List<StorageClass> storageClassesList = storageClassesService.getAllStorageClasses();

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "获取 StorageClass 列表成功");
        result.put("data", storageClassesList);

        return JSON.toJSONString(result);
    }
}

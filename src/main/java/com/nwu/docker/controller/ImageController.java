package com.nwu.docker.controller;

import com.alibaba.fastjson.JSON;
import com.nwu.docker.entities.ImageDefinition;
import com.nwu.docker.service.ImageService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author Rex Joush
 * @time 2021.05.11 19:58
 */
@RequestMapping("/images")
@RestController
public class ImageController {

    @Resource
    private ImageService imageService;

    @RequestMapping("/getAllImages")
    public String getAllImages(){
        List<ImageDefinition> images = imageService.getAllImages();

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "获取镜像列表成功");
        result.put("data", images);

        return JSON.toJSONString(result);
    }

}

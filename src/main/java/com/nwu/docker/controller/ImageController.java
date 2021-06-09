package com.nwu.docker.controller;

import com.alibaba.fastjson.JSON;
import com.nwu.docker.entities.ImageDefinition;
import com.nwu.docker.entities.ImageDetailsDefinition;
import com.nwu.docker.service.ImageService;
import com.nwu.util.DockerUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
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
    public String getAllImages() {
        List<ImageDefinition> images = imageService.getAllImages();

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "获取镜像列表成功");
        result.put("data", images);

        return JSON.toJSONString(result);
    }

    @RequestMapping("/getImageById")
    public String getImageById(@RequestParam("id") String id) {
        ImageDetailsDefinition image = imageService.getImageById(id);

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "获取镜像详情成功");
        result.put("data", image);

        return JSON.toJSONString(result);
    }

    @RequestMapping("/delImage")
    public String delImage() {

        Map<String, Object> result = new HashMap<>();

        result.put("code", 1200);
        result.put("message", "获取镜像列表成功");

        return JSON.toJSONString(result);
    }

    @PostMapping("/uploadImage")
    public String upload(@RequestParam("image") MultipartFile image) {

        Map<String, Object> result = new HashMap<>();

        // 接收到镜像，直接上传
        if (!image.isEmpty()){
            try {
                Void exec = DockerUtils.docker.loadImageCmd(image.getInputStream()).exec();
                result.put("code", 1200);
                result.put("message", "镜像上传成功");
            } catch (IOException e) {
                result.put("code", 1202);
                result.put("message", e.getMessage());
            }
        }
        else {
            result.put("code", 1202);
            result.put("message", "镜像上传失败");
        }
        return JSON.toJSONString(result);
    }

}

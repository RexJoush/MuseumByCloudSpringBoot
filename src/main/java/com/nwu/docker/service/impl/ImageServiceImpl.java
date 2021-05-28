package com.nwu.docker.service.impl;

import com.nwu.docker.entities.ImageDefinition;
import com.nwu.docker.entities.ImageDetailsDefinition;
import com.nwu.docker.service.ImageService;
import com.nwu.util.DockerUtils;
import com.github.dockerjava.api.model.Image;
import com.nwu.util.TimeUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Rex Joush
 * @time 2021.05.11 21:52
 */
@Service
public class ImageServiceImpl implements ImageService {


    @Override
    public List<ImageDefinition> getAllImages() {
        List<ImageDefinition> result = new ArrayList<>();

        List<Image> images = DockerUtils.docker.listImagesCmd().exec();

        for (Image image : images) {
            ImageDefinition imageDefinition = new ImageDefinition();

            // 设置镜像名称
            imageDefinition.setName(image.getRepoDigests()[0].split("@")[0]);

            // 设置 id
            imageDefinition.setId(image.getId());

            // 设置标签
            if (image.getRepoTags() != null) {
                imageDefinition.setTags(image.getRepoTags());
            } else {
                imageDefinition.setTags(new String[]{"<none>"});
            }

            // 设置大小
            imageDefinition.setSize(image.getSize());

            // 设置创建时间
            /*
                此处经过测试，发现 image.getCreated()
                获取的值是从 1970.01.01 到创建时刻的秒值，而非毫秒值
                故乘 1000 计算时间
             */
            imageDefinition.setTime(TimeUtils.sdf.format(new Date(image.getCreated() * 1000)));

            // 加入结果列表
            result.add(imageDefinition);
        }

        // 返回结果
        return result;
    }

    public ImageDetailsDefinition getImageById(String id) {

        ImageDetailsDefinition definition = new ImageDetailsDefinition();

        RestTemplate template = new RestTemplate();

        String layers = template.getForObject("http://192.168.29.145:2376/images/" + id + "/history", String.class);

        definition.setLayers(layers);
        definition.setImage(DockerUtils.docker.inspectImageCmd(id).exec());

        return definition;
    }

    public static void main(String[] args) {
        System.out.println(new ImageServiceImpl().getImageById("sha256:f0b8a9a541369db503ff3b9d4fa6de561b300f7363920c2bff4577c6c24c5cf6"));
    }
}

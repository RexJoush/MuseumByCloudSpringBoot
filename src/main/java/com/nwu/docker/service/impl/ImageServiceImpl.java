package com.nwu.docker.service.impl;

import com.nwu.docker.entities.ImageDefinition;
import com.nwu.docker.service.ImageService;
import com.nwu.docker.util.DockerUtils;
import com.github.dockerjava.api.model.Image;
import com.nwu.util.TimeUtils;
import org.springframework.stereotype.Service;

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

            // 设置 id
            imageDefinition.setId(image.getId());

            // 设置标签
            if (image.getRepoTags() != null){
                imageDefinition.setTags(image.getRepoTags());
            } else {
                imageDefinition.setTags(null);
            }

            // 设置大小
            imageDefinition.setSize(image.getSize());

            // 设置创建时间
            imageDefinition.setTime(TimeUtils.sdf.format(new Date(image.getCreated())));

            // 加入结果列表
            result.add(imageDefinition);
        }

        // 返回结果
        return result;
    }
}

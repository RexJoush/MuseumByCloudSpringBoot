package com.nwu.docker.service;

import com.nwu.docker.entities.ImageDefinition;
import com.nwu.docker.entities.ImageDetailsDefinition;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Rex Joush
 * @time 2021.05.11 21:52
 */


public interface ImageService {

    /**
     * 获取所有镜像列表
     * @return 镜像列表
     */
    List<ImageDefinition> getAllImages();

    /**
     * 通过镜像 id 获取镜像详情
     * @param id 镜像 id
     * @return 详情信息
     */
    ImageDetailsDefinition getImageById(String id);

}

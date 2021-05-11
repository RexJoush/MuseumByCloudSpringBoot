package com.nwu.docker.service;

import com.nwu.docker.entities.ImageDefinition;
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

}

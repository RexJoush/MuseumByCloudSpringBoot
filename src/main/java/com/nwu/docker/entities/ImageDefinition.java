package com.nwu.docker.entities;

import lombok.Data;

/**
 * @author Rex Joush
 * @time 2021.05.11 21:53
 */
@Data
public class ImageDefinition {

    private String name;        // 镜像名称
    private String id;          // 镜像id
    private String[] tags;      // 镜像标签
    private double size;        // 镜像大小
    private String time;        // 镜像创建时间
}

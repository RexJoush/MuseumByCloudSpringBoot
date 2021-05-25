package com.nwu.docker.entities;

import com.github.dockerjava.api.command.InspectImageResponse;
import lombok.Data;

/**
 * @author Rex Joush
 * @time 2021.05.16 20:47
 */

@Data
public class ImageDetailsDefinition {

    private String layers;
    private InspectImageResponse image;

}

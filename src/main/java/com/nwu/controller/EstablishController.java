package com.nwu.controller;

/**
 * @author Rex Joush
 * @time 2021.03.22
 */

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Date;

/**
 * 创建资源的 controller 层
 */
@RestController
@RequestMapping("/establish")
public class EstablishController {

    @PostMapping(value = "/file2")
    public String uploadFile(@RequestParam("file") MultipartFile file){
        String fileName = file.getOriginalFilename();
        String suffix = fileName.substring(fileName.lastIndexOf('.'));
        String newFileName = new Date().getTime() + suffix;
        System.out.println(newFileName);
        String path = "E:/";
        File newFile = new File(path + newFileName);
        try {
            file.transferTo(newFile);
            return "成功";
        }
        catch (Exception e){
            e.printStackTrace();
            return "失败";
        }
    }
}

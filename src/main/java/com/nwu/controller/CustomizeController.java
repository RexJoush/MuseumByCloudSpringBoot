package com.nwu.controller;

/**
 * @author Rex Joush
 * @time 2021.03.22
 */

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 自定义资源的 controller 层
 */
@RestController
@RequestMapping("/customize")
public class CustomizeController {

    @RequestMapping("/index")
    public String index(){
        System.out.println("index 111");
        return "Hello Index";
    }

    @RequestMapping("/home")
    public String home(){
        System.out.println("home 111");
        return "Hello Home";
    }

    @RequestMapping("/level1/{id}")
    public String level1(@PathVariable("id") int id){
        return "Hello level1 " + id;
    }

    @RequestMapping("/level2/{id}")
    public String level2(@PathVariable("id") int id){
        return "Hello level2 " + id;
    }

    @RequestMapping("/level3/{id}")
    public String level3(@PathVariable("id") int id){
        return "Hello level3 " + id;
    }

}

package com.nwu.controller;

import com.alibaba.fastjson.JSON;
import com.nwu.entity.User;
import com.nwu.service.UserService;
import com.nwu.util.TokenUtils;
import com.nwu.util.impl.TokenDetailImpl;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Rex Joush
 * @time 2021.03.18
 */

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @Resource
    private TokenUtils tokenUtils;


    @RequestMapping("/login")
    public Map<String, Object> login(String username, String password) {

        System.out.println(password);
        System.out.println(username);

        // 根据用户名密码进行查询用户
        User user = userService.login(username.trim(), DigestUtils.sha256Hex(password.trim()));

        // 封装返回结果
        Map<String, Object> result = new HashMap<>();

        // 生成 token
        String token = tokenUtils.generateToken(new TokenDetailImpl(username));

        // 如果查到用户，说明登陆成功
        if (user != null) {
            result.put("token", token);
            result.put("code",1200);
            result.put("message","登录成功");
            result.put("data", user);
        }
        // 否则返回登陆失败
        else {
            result.put("code",1202);
            result.put("message","用户名或密码错误");
            result.put("data", null);
        }

        return result;
    }

    @RequestMapping("/info")
    public String getUserInfo(){

        System.out.println("info");
        Map<String, Object> result = new HashMap<>();


        return JSON.toJSONString("User Info");

    }

    @RequestMapping("/logout")
    public String logout(){

        System.out.println("logout");


        Map<String, Object> result = new HashMap<>();
        result.put("code",2200);
        result.put("message","登录成功");
        result.put("data", "success");
        return JSON.toJSONString(result);

    }

    @RequestMapping("/getUsers")
    public String findAll(HttpServletRequest request){

        System.out.println("getUsers");

        String token = request.getHeader("token");

        String username = tokenUtils.getUsernameFromToken(token);

        System.out.println(username);


        List<User> users = userService.findAll();

        Map<String, Object> result = new HashMap<>();
        result.put("data", users);

        return JSON.toJSONString(result);

    }

}

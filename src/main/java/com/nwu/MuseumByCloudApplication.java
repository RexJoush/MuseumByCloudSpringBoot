package com.nwu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.CrossOrigin;

@SpringBootApplication
@CrossOrigin // 配置跨域支持
@EnableAsync(proxyTargetClass = true) // 配置多线程支持

public class MuseumByCloudApplication {

    public static void main(String[] args) {
        SpringApplication.run(MuseumByCloudApplication.class, args);
    }

}

package com.nwu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;

@SpringBootApplication
@CrossOrigin
public class MuseumByCloudApplication {

    public static void main(String[] args) {
        SpringApplication.run(MuseumByCloudApplication.class, args);
    }

}

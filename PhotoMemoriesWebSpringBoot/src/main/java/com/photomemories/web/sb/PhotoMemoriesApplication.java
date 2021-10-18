package com.photomemories.web.sb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication
public class PhotoMemoriesApplication {
    public static void main(String[] args){
        SpringApplication.run(PhotoMemoriesApplication.class, args);
    }
}

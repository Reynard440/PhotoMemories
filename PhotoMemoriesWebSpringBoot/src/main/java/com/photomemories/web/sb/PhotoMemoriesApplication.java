package com.photomemories.web.sb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
//TODO Try and get the application to function without excluding the DataSourceAutoConfiguration
public class PhotoMemoriesApplication {
    public static void main(String[] args){
        SpringApplication.run(PhotoMemoriesApplication.class, args);
    }
}

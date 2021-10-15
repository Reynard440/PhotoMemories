package com.photomemories.web.sb.config;

import com.photomemories.logic.config.LogicConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Import({LogicConfig.class})
@Configuration
@ComponentScan(basePackages = {
        "com.photomemories.web.sb.controller"
})
public class WebConfig {

}

package com.photomemories.logic.config;

import com.photomemories.translator.config.TranslatorConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Import({TranslatorConfig.class})
@Configuration
@ComponentScan(basePackages = {
        "com.photomemories.logic.flow"
})
public class LogicConfig {

}

package com.photomemories.translator.config;

import com.photomemories.repo.config.RepositoryConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Import({RepositoryConfig.class})
@Configuration
@ComponentScan(basePackages = {
        "com.photomemories.translator"
})
public class TranslatorConfig {

}

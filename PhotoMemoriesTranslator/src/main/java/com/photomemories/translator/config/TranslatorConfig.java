package com.photomemories.translator.config;

import com.photomemories.aws.config.AwsConfig;
import com.photomemories.repo.config.RepositoryConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Import({RepositoryConfig.class, AwsConfig.class})
@Configuration
@ComponentScan(basePackages = {
        "com.photomemories.translator",
        "com.photomemories.aws"
})
public class TranslatorConfig {

}

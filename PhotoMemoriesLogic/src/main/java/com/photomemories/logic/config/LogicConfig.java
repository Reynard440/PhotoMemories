package com.photomemories.logic.config;

import com.photomemories.translator.config.TranslatorConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Import({TranslatorConfig.class})
@Configuration
@ComponentScan(basePackages = {
        "com.photomemories.logic.flow"
})
public class LogicConfig {
    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }
}

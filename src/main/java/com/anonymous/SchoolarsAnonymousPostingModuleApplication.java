package com.anonymous;

import com.anonymous.properties.S3ConfigurationProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableConfigurationProperties(S3ConfigurationProperties.class)
@EnableJpaAuditing
@EnableCaching
@EnableScheduling
public class SchoolarsAnonymousPostingModuleApplication {

    public static void main(String[] args) {
        SpringApplication.run(SchoolarsAnonymousPostingModuleApplication.class, args);
    }
}

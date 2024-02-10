package com.example.schoolarsanonymouspostingmodule;

import com.example.schoolarsanonymouspostingmodule.properties.S3ConfigurationProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
@EnableConfigurationProperties(S3ConfigurationProperties.class)
public class SchoolarsAnonymousPostingModuleApplication {


    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(SchoolarsAnonymousPostingModuleApplication.class, args);
    }

}

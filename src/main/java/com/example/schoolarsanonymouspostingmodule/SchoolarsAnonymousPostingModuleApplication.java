package com.example.schoolarsanonymouspostingmodule;

import com.example.schoolarsanonymouspostingmodule.properties.S3ConfigurationProperties;
import com.example.schoolarsanonymouspostingmodule.service.S3StorageService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Paths;

@SpringBootApplication
@EnableConfigurationProperties(S3ConfigurationProperties.class)
public class SchoolarsAnonymousPostingModuleApplication {


    public static void main(String[] args) {

        ApplicationContext context = SpringApplication.run(SchoolarsAnonymousPostingModuleApplication.class, args);

    }

}

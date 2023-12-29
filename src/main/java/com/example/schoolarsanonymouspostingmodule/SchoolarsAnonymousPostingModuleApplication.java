package com.example.schoolarsanonymouspostingmodule;

import com.example.schoolarsanonymouspostingmodule.service.StorageService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class SchoolarsAnonymousPostingModuleApplication {


    public static void main(String[] args) {

        ApplicationContext context = SpringApplication.run(SchoolarsAnonymousPostingModuleApplication.class, args);
        StorageService storageService = context.getBean(StorageService.class);
        storageService.getPostsFileNames();
    }

}

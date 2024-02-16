package com.example.schoolarsanonymouspostingmodule;


import com.example.schoolarsanonymouspostingmodule.service.S3StorageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@SpringBootTest
@AutoConfigureMockMvc
public class StorageServiceTest {
    private static String url;

    @Autowired
    private S3StorageService storageService;

    @Test
    public void uploadTest() throws IOException {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "projfi1.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                Files.readAllBytes(Paths.get("projfi1.jpg"))
        );
        url = storageService.upload(file);
        System.out.println();
        System.out.println();
    }

    //Works well
    @Test
    public void deleteTest() {
        storageService.delete(url);
    }
}

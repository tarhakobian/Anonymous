package com.example.schoolarsanonymouspostingmodule;

import com.example.schoolarsanonymouspostingmodule.model.dto.request.LoginRequest;
import com.example.schoolarsanonymouspostingmodule.model.dto.request.UserRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicReference;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PostControllerTest {

    private static final ObjectMapper mapper = new ObjectMapper();
    private static MockMvc mockMvc;

    private static String adminJwtToken = "";
    private static String jwtToken = "";

    @BeforeAll
    public static void setJwtToken(@Autowired MockMvc mockMvc) throws Exception {
        UserRequest userRequest = new UserRequest();
        userRequest.setEmail("taronhakobya2@student.glendale.edu");
        userRequest.setPassword("123456");

        // create a user
        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userRequest)))
                .andExpect(status().isCreated());

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(userRequest.getEmail());
        loginRequest.setPassword(userRequest.getPassword());

        AtomicReference<String> jwtTokenReference = new AtomicReference<>("");
        //login and get jwt
        mockMvc.perform(post("/login").
                        contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk()).andDo(result ->
                        jwtTokenReference.set(result.getResponse().getHeader("Authorization"))
                );

        jwtToken = jwtTokenReference.get();

        LoginRequest adminLoginRequest = new LoginRequest();
        adminLoginRequest.setEmail("admin@schoolars.education");
        adminLoginRequest.setPassword("tarondavit");

        AtomicReference<String> adminJwtTokenReference = new AtomicReference<>("");
        //login and get jwt
        mockMvc.perform(post("/login").
                        contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(adminLoginRequest)))
                .andExpect(status().isOk()).andDo(result ->
                        adminJwtTokenReference.set(result.getResponse().getHeader("Authorization"))
                );

        adminJwtToken = adminJwtTokenReference.get();


    }

    @Autowired
    public void setMockMvc(MockMvc mockBean) {
        mockMvc = mockBean;
    }

    @Test
    @Order(1)
    public void postCreateTest() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "projfi1.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                Files.readAllBytes(Paths.get("projfi1.jpg"))
        );

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", jwtToken);

        mockMvc.perform(MockMvcRequestBuilders.multipart("/anonymous-posts/")
                        .file(file)
                        .param("usernamePublic", "true")
                        .headers(headers))
                .andExpect(status().isCreated());

        mockMvc.perform(MockMvcRequestBuilders.multipart("/anonymous-posts/")
                        .file(file)
                        .param("usernamePublic", "true")
                        .headers(headers))
                .andExpect(status().isCreated());

        mockMvc.perform(MockMvcRequestBuilders.multipart("/anonymous-posts/")
                        .file(file)
                        .param("usernamePublic", "true")
                        .headers(headers))
                .andExpect(status().isCreated());
    }

    @Test
    @Order(2)
    public void editTest() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "proj73.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                Files.readAllBytes(Paths.get("proj73.jpg"))
        );

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", jwtToken);

        mockMvc.perform(MockMvcRequestBuilders.multipart("/anonymous-posts/51")
                        .file(file)
                        .param("usernamePublic", "true")
                        .headers(headers)
                        .with(request -> {
                            request.setMethod(HttpMethod.PUT.name());
                            return request;
                        })
                )
                .andExpect(status().isOk());
    }


    @Test
    @Order(3)
    public void deleteTest() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", jwtToken);

        mockMvc.perform(delete("/anonymous-posts/50")
                        .headers(headers))
                .andExpect(status().isOk());
    }

    @Test
    @Order(4)
    public void adminDeleteTest() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", adminJwtToken);

        mockMvc.perform(delete("/anonymous-posts/3")
                        .headers(headers))
                .andExpect(status().isOk());
    }

    @Test
    @Order(5)
    void likeTest() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", jwtToken);

        mockMvc.perform(post("/anonymous-posts/like/1").headers(headers)).andExpect(status().isOk());
    }

    @Test
    @Order(5)
    public void unlikeTest() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", jwtToken);

        mockMvc.perform(delete("/anonymous-posts/like/1").headers(headers)).andExpect(status().isOk());
    }


}

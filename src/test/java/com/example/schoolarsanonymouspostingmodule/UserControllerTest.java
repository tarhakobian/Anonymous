package com.example.schoolarsanonymouspostingmodule;

import com.example.schoolarsanonymouspostingmodule.model.dto.request.LoginRequest;
import com.example.schoolarsanonymouspostingmodule.model.dto.request.UserRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.atomic.AtomicReference;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private static final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void registerTest() throws Exception {

        UserRequest userRequest = new UserRequest();
        userRequest.setEmail("taronhakobyan111@student.glendale.edu");
        userRequest.setPassword("123456");

        // Perform the Post request
        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userRequest)))
                .andExpect(status().isCreated());


        //duplicate email users
        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userRequest)))
                .andExpect(status().isConflict());


        userRequest.setEmail("taron1@studen.glendale.edu");

        //wrong format of email
        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userRequest)))
                .andExpect(status().isBadRequest());

        // wrong format of password
        userRequest.setEmail("taron2@student.glendale.edu");
        userRequest.setPassword("123");

        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void loginTest() throws Exception {
        UserRequest userRequest = new UserRequest();
        userRequest.setEmail("taronhakobyan1111234@student.glendale.edu");
        userRequest.setPassword("123456");

        // create a user
        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userRequest)))
                .andExpect(status().isCreated());

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(userRequest.getEmail());
        loginRequest.setPassword(userRequest.getPassword());

        //login and get jwt
        mockMvc.perform(post("/login").
                        contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(loginRequest)))
                .andExpect(header().exists("Authorization"));

    }

    @Test
    public void changeUsernameTest() throws Exception {
        UserRequest userRequest = new UserRequest();
        userRequest.setEmail("taronhakobyan11112384t78475t@student.glendale.edu");
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

        String jwtToken = jwtTokenReference.get();

        //set Authorization header
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", jwtToken);

        //perform change-username
        mockMvc.perform(patch("/users/change-username")
                        .headers(headers)
                        .param("username", "Taron Hakobyan"))
                .andExpect(status().isOk());

        UserRequest userRequest2 = new UserRequest();
        userRequest2.setEmail("taronhakobyan11112384t78475tsdasdas@student.glendale.edu");
        userRequest2.setPassword("123456");

        // create a user
        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userRequest2)))
                .andExpect(status().isCreated());

        LoginRequest loginRequest2 = new LoginRequest();
        loginRequest.setEmail(userRequest2.getEmail());
        loginRequest.setPassword(userRequest2.getPassword());

        AtomicReference<String> jwtTokenReference2 = new AtomicReference<>("");
        //login and get jwt
        mockMvc.perform(post("/login").
                        contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk()).andDo(result ->
                        jwtTokenReference2.set(result.getResponse().getHeader("Authorization"))
                );

        String jwtToken2 = jwtTokenReference2.get();

        //set Authorization header
        HttpHeaders headers2 = new HttpHeaders();
        headers2.set("Authorization", jwtToken2);

        //perform change-username
        mockMvc.perform(patch("/users/change-username")
                        .headers(headers2)
                        .param("username", "Taron Hakobyan"))
                .andExpect(status().isConflict());
    }


}
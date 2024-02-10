package com.example.schoolarsanonymouspostingmodule;

import com.example.schoolarsanonymouspostingmodule.model.dto.request.CommentRequest;
import com.example.schoolarsanonymouspostingmodule.model.dto.request.LoginRequest;
import com.example.schoolarsanonymouspostingmodule.model.dto.request.UserRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.concurrent.atomic.AtomicReference;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CommentControllerTest {
    private static MockMvc mockMvc;

    @Autowired
    public void setMockMvc(MockMvc mockBean) {
        mockMvc = mockBean;
    }

    private static String jwtToken = "";

    private static final ObjectMapper mapper = new ObjectMapper();

    @BeforeAll
    public static void setJwtToken(@Autowired MockMvc mockMvc) throws Exception {
        UserRequest userRequest = new UserRequest();
        userRequest.setEmail("taronhakobyan111123562131231231231@student.glendale.edu");
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
    }

    @Test
    public void commentTest() throws Exception {
        CommentRequest commentRequest = new CommentRequest();

        commentRequest.setPostId(1);
        commentRequest.setContent("Barev dzez");

        //attempt comment without authorization token : expects UNAUTHORIZED
        mockMvc.perform(post("/anonymous-posts/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(commentRequest)))
                .andExpect(status().isForbidden());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", jwtToken);

        //Valid request to comment expecting OK
        mockMvc.perform(post("/anonymous-posts/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .headers(headers)
                        .content(mapper.writeValueAsString(commentRequest)))
                .andExpect(status().isCreated());

        //Valid id but doesn't exist expects NOT FOUND
        commentRequest.setPostId(100);

        mockMvc.perform(post("/anonymous-posts/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .headers(headers)
                        .content(mapper.writeValueAsString(commentRequest)))
                .andExpect(status().isNotFound());


        //negative id expecting BAD REQUEST
        commentRequest.setPostId(-1);

        mockMvc.perform(post("/anonymous-posts/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .headers(headers)
                        .content(mapper.writeValueAsString(commentRequest)))
                .andExpect(status().isBadRequest());

        //null id expecting BAD REQUEST
        commentRequest.setPostId(null);

        mockMvc.perform(post("/anonymous-posts/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .headers(headers)
                        .content(mapper.writeValueAsString(commentRequest)))
                .andExpect(status().isBadRequest());

        //invalid context expecting BAD REQUEST
        commentRequest.setPostId(1);
        commentRequest.setContent(null);

        mockMvc.perform(post("/anonymous-posts/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .headers(headers)
                        .content(mapper.writeValueAsString(commentRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void replyTest() throws Exception {
        CommentRequest commentRequest = new CommentRequest();

        commentRequest.setPostId(1);
        commentRequest.setParentCommentId(1);
        commentRequest.setContent("reply");

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", jwtToken);

        //Valid request to comment expecting OK
        mockMvc.perform(post("/anonymous-posts/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .headers(headers)
                        .content(mapper.writeValueAsString(commentRequest)))
                .andExpect(status().isCreated());
    }

    @Test
    public void deleteCommentTest() throws Exception {
        CommentRequest commentRequest = new CommentRequest();

        commentRequest.setPostId(1);
        commentRequest.setContent("Gago");

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", jwtToken);

        //Valid request to comment expecting OK and set commentId
        int commentId = Integer.parseInt(mockMvc.perform(post("/anonymous-posts/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .headers(headers)
                        .content(mapper.writeValueAsString(commentRequest)))
                .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString());

        // perform valid comment delete request : expects OK
        mockMvc.perform(delete("/anonymous-posts/comment/" + commentId)
                        .headers(headers))
                .andExpect(status().isOk());

        //perform invalid comment delete request with invalid commentId
        mockMvc.perform(delete("/anonymous-posts/comment/" + 100)
                        .headers(headers))
                .andExpect(status().isNotFound());
    }

    @Test
    public void editCommentTest() throws Exception {
        CommentRequest commentRequest = new CommentRequest();

        commentRequest.setPostId(3);
        commentRequest.setContent("Gago");

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", jwtToken);

        //Valid request to comment expecting OK and set commentId
        int commentId = Integer.parseInt(mockMvc.perform(post("/anonymous-posts/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .headers(headers)
                        .content(mapper.writeValueAsString(commentRequest)))
                .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString());

        //valid request to edit comment
        mockMvc.perform(patch("/anonymous-posts/comment/" + commentId)
                        .headers(headers)
                        .param("content", "Vaxo"))
                .andExpect(status().isOk());

        //invalid request to edit comment with content == null
        mockMvc.perform(patch("/anonymous-posts/comment/" + commentId)
                        .headers(headers)
                        .param("content", (String) null))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void likeCommentTest() throws Exception {
        CommentRequest commentRequest = new CommentRequest();

        commentRequest.setPostId(3);
        commentRequest.setContent("Gago");

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", jwtToken);

        //Valid request to comment expecting OK and set commentId
        int commentId = Integer.parseInt(mockMvc.perform(post("/anonymous-posts/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .headers(headers)
                        .content(mapper.writeValueAsString(commentRequest)))
                .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString());

        //like valid
        mockMvc.perform(post("/anonymous-posts/comment/like/" + commentId)
                        .headers(headers))
                .andExpect(status().isOk());

        //like invalid commentId
        mockMvc.perform(post("/anonymous-posts/comment/like/" + 500)
                        .headers(headers))
                .andExpect(status().isNotFound());

        //like invalid no Authorization header
        mockMvc.perform(post("/anonymous-posts/comment/like/" + 4))
                .andExpect(status().isForbidden());
    }

    @Test
    public void unlikeCommentTest() throws Exception {
        CommentRequest commentRequest = new CommentRequest();

        commentRequest.setPostId(3);
        commentRequest.setContent("Gago");

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", jwtToken);

        //Valid request to comment expecting OK and set commentId
        int commentId = Integer.parseInt(mockMvc.perform(post("/anonymous-posts/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .headers(headers)
                        .content(mapper.writeValueAsString(commentRequest)))
                .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString());

        //like valid
        mockMvc.perform(post("/anonymous-posts/comment/like/" + commentId)
                        .headers(headers))
                .andExpect(status().isOk());

        //unlike valid
        mockMvc.perform(delete("/anonymous-posts/comment/like/" + commentId)
                        .headers(headers))
                .andExpect(status().isOk());

        //unlike invalid commentId
        mockMvc.perform(delete("/anonymous-posts/comment/like/" + 500)
                        .headers(headers))
                .andExpect(status().isNotFound());

        //unlike invalid no Authorization header
        mockMvc.perform(delete("/anonymous-posts/comment/like/" + 4))
                .andExpect(status().isForbidden());
    }

}

package com.example.schoolarsanonymouspostingmodule.service;

import com.example.schoolarsanonymouspostingmodule.exception.PostNotFoundException;
import com.example.schoolarsanonymouspostingmodule.model.dto.request.PostRequest;
import com.example.schoolarsanonymouspostingmodule.model.dto.responce.PostResponse;
import com.example.schoolarsanonymouspostingmodule.model.entity.PostEntity;
import com.example.schoolarsanonymouspostingmodule.model.entity.UserEntity;
import com.example.schoolarsanonymouspostingmodule.repository.PostRepository;
import com.example.schoolarsanonymouspostingmodule.util.Mapper;
import com.example.schoolarsanonymouspostingmodule.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final CommentService commentService;
    private final PostRepository postRepository;
    private final UserService userService;


    public PostResponse getById(Integer postId) {
        PostEntity entity = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
        return Mapper.mapPost(entity);
    }

    public List<PostResponse> getAll() {
        return postRepository.findAllOrderById().stream()
                .peek(e -> e.setComments(commentService.getCommentsForPost(e)))
                .map(Mapper::mapPost).toList();
    }

    //TODO: upload to S3
    public Long create(PostRequest request) {
        UserEntity userEntity = userService.getLoggedInUser();

        PostEntity postEntity = new PostEntity();

        postEntity.setUrl("temporary url");
        postEntity.setPublisher(userEntity);
        postEntity.setUsernamePublic(request.isUsernamePublic());

        userEntity.getPosts().add(postEntity);

        userService.save(userEntity);
        postRepository.save(postEntity);

        return postEntity.getId();
    }

    public void edit(Integer postId, PostRequest request) {
        PostEntity postEntity = postRepository.findById(postId)
                .filter(entity -> SecurityUtil.ensureOwnership(entity.getPublisher()))
                .orElseThrow(PostNotFoundException::new);

        //TODO rewrite after integrating with S3
        postEntity.setUrl("temporary url (updated or no)");
        postEntity.setUsernamePublic(request.isUsernamePublic());

        postRepository.save(postEntity);
    }

    public void delete(Integer postId) {
        PostEntity entity = postRepository.findById(postId)
                .filter(e -> SecurityUtil.ensureOwnership(e.getPublisher()))
                .orElseThrow(PostNotFoundException::new);

        postRepository.delete(entity);
    }

    public void like(Integer postId) {
        PostEntity postEntity = postRepository.findById(postId)
                .orElseThrow(PostNotFoundException::new);

        UserEntity userEntity = userService.getLoggedInUser();

        userEntity.getLikedPosts().add(postEntity);
        postEntity.getLikedBy().add(userEntity);

        postRepository.save(postEntity);
        userService.save(userEntity);
    }

    public void unLike(Integer postId) {
        PostEntity postEntity = postRepository.findById(postId)
                .orElseThrow(PostNotFoundException::new);

        UserEntity userEntity = userService.getLoggedInUser();

        postEntity.getLikedBy().remove(userEntity);
        userEntity.getLikedPosts().remove(postEntity);

        userService.save(userEntity);
        postRepository.save(postEntity);
    }
}

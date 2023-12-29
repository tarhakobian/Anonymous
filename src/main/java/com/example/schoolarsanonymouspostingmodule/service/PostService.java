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
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private static final String S3_DIRECTORY = "posts";

    private final CommentService commentService;
    private final PostRepository postRepository;
    private final UserService userService;
    private final S3StorageService storageService;


    public PostResponse getById(Integer postId) {
        PostEntity entity = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
        return Mapper.mapPost(entity);
    }

    public List<PostResponse> getAll(Integer pageNumber, Integer size) {
        return postRepository.findAllOrderByIdDesc(PageRequest.of(pageNumber, size)).stream()
                .peek(e -> e.setComments(commentService.getCommentsForPost(e)))
                .map(Mapper::mapPost).toList();
    }

    //TODO: upload to S3
    public Long create(PostRequest request) {
        UserEntity userEntity = userService.getLoggedInUser();

        PostEntity postEntity = new PostEntity();


        postEntity.setUrl(storageService.upload(request.getFile(), S3_DIRECTORY));
        postEntity.setPublisher(userEntity);
        postEntity.setUsernamePublic(request.isUsernamePublic());

        userEntity.getPosts().add(postEntity);

        userService.save(userEntity);
        postRepository.save(postEntity);

        return postEntity.getId();
    }

    //TODO: s3 flow?
    public void edit(Integer postId, PostRequest request) {
        PostEntity postEntity = postRepository.findById(postId)
                .filter(entity -> SecurityUtil.ensureOwnership(entity.getPublisher()))
                .orElseThrow(PostNotFoundException::new);

        postEntity.setUrl("temporary url (updated or no)");
        postEntity.setUsernamePublic(request.isUsernamePublic());

        postRepository.save(postEntity);
    }

    //TODO: add s3 flow
    public void delete(Integer postId) {
        PostEntity entity = postRepository.findById(postId)
                .filter(e -> SecurityUtil.ensureOwnership(e.getPublisher()))
                .orElseThrow(PostNotFoundException::new);

        storageService.delete(entity.getUrl());

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

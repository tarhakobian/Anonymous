package com.anonymous.service;

import com.anonymous.model.dto.responce.PostResponse;
import com.anonymous.model.entity.PostEntity;
import com.anonymous.model.entity.UserEntity;
import com.anonymous.repository.PostRepository;
import com.anonymous.util.Mapper;
import com.anonymous.exception.PostNotFoundException;
import com.anonymous.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Service class for managing posts in the system.
 * <p>
 * Author: Taron Hakobyan
 */
@Service
@RequiredArgsConstructor
public class PostService {

    private static final String S3_DIRECTORY = "posts";

    private final CommentService commentService;
    private final PostRepository postRepository;
    private final UserService userService;
    private final S3StorageService storageService;

    /**
     * Retrieves a post by its identifier.
     *
     * @param postId The identifier of the post to be retrieved.
     * @return PostResponse representing the retrieved post.
     * @throws PostNotFoundException if the post is not found.
     */
    public PostResponse getById(Integer postId) {
        PostEntity entity = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
        return Mapper.mapPost(entity);
    }

    /**
     * Retrieves all posts with associated entities (comments, likedBy) ordered by post id in descending order,
     * along with comments for each post.
     *
     * @param pageNumber Page number for pagination.
     * @param size       Number of posts to be retrieved per page.
     * @return List of PostResponse representing the retrieved posts.
     */
    public List<PostResponse> getAll(Integer pageNumber, Integer size) {
        return postRepository.findAllOrderByIdDesc(PageRequest.of(pageNumber, size)).stream()
                .peek(e -> e.setComments(commentService.getCommentsForPost(e)))
                .map(Mapper::mapPost)
                .toList();
    }

    /**
     * Retrieves all posts liked by the logged-in user.
     *
     * @return List of PostResponse representing the liked posts.
     */
    public List<PostResponse> getLikedPosts() {
        UserEntity userEntity = userService.getLoggedInUser();
        return userEntity.getLikedPosts().stream().map(Mapper::mapPost).toList();
    }

    /**
     * Creates a new post with the specified request.
     * The post is associated with the logged-in user and uploaded to S3 storage.
     *
     * @param file           The file representing the post content.
     * @param usernamePublic Whether the username should be public.
     * @return The identifier of the created post.
     */
    @Transactional
    public Integer create(MultipartFile file, boolean usernamePublic) {
        UserEntity userEntity = userService.getLoggedInUser();

        PostEntity postEntity = new PostEntity();
        postEntity.setUrl(storageService.upload(file, S3_DIRECTORY));
        postEntity.setPublisher(userEntity);
        postEntity.setUsernamePublic(usernamePublic);

        userEntity.getPosts().add(postEntity);
        userService.save(userEntity);

        postRepository.save(postEntity);
        return postEntity.getId();
    }

    /**
     * Updates an existing post with the specified request.
     * The post URL is temporarily set, and S3 flow is pending implementation.
     *
     * @param postId         The identifier of the post to be edited.
     * @param file           The file representing the updated post content.
     * @param usernamePublic Whether the username should be public.
     */
    public void edit(Integer postId, MultipartFile file, boolean usernamePublic) {
        PostEntity postEntity = postRepository.findById(postId)
                .filter(entity -> SecurityUtil.ensureOwnership(entity.getPublisher()))
                .orElseThrow(PostNotFoundException::new);

        if (file != null) {
            String url = storageService.upload(file, S3_DIRECTORY);
            storageService.delete(url);
            postEntity.setUrl(url);
        }

        postEntity.setUsernamePublic(usernamePublic);
        postRepository.save(postEntity);
    }

    /**
     * Deletes an existing post by its identifier.
     * The post URL is deleted from S3 storage, and the post entity is marked as deleted.
     *
     * @param postId The identifier of the post to be deleted.
     */
    @Transactional
    public void delete(Integer postId) {
        PostEntity entity = postRepository.findById(postId)
                .filter(e -> SecurityUtil.ensureOwnership(e.getPublisher()))
                .orElseThrow(PostNotFoundException::new);

        entity.setIsDeleted(true);
        postRepository.save(entity);
    }

    /**
     * Likes a specific post.
     *
     * @param postId The identifier of the post to be liked.
     */
    @Transactional
    public void like(Integer postId) {
        PostEntity postEntity = postRepository.findById(postId)
                .orElseThrow(PostNotFoundException::new);

        UserEntity userEntity = userService.getLoggedInUser();
        userEntity.getLikedPosts().add(postEntity);
        postEntity.getLikedBy().add(userEntity);

        postRepository.save(postEntity);
        userService.save(userEntity);
    }

    /**
     * Removes the like from a specific post.
     *
     * @param postId The identifier of the post from which the like is to be removed.
     */
    @Transactional
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

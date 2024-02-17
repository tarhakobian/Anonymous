package com.example.schoolarsanonymouspostingmodule.schdeuled_tasks;

import com.example.schoolarsanonymouspostingmodule.model.entity.PostEntity;
import com.example.schoolarsanonymouspostingmodule.repository.PostRepository;
import com.example.schoolarsanonymouspostingmodule.service.S3StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class PostCleanUp {

    private final PostRepository postRepository;


    private final S3StorageService storageService;


    //4AM PST every day
    @Scheduled(cron = "0 0 4 * * *", zone = "America/Los_Angeles")
    public void deleteMarkedPosts() {
        Set<PostEntity> deletedPosts = postRepository.findByIsDeletedTrue();

        deletedPosts.forEach(post -> storageService.delete(post.getUrl()));

        postRepository.deleteAll(deletedPosts);
    }
}

package com.example.schoolarsanonymouspostingmodule.service;

import com.example.schoolarsanonymouspostingmodule.model.entity.FrameEntity;
import com.example.schoolarsanonymouspostingmodule.repository.FrameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class FrameService {

    private final FrameRepository repository;

    public String getCurrentFrameUrl() {
        FrameEntity entity = repository.findFirstByOrderByIdAsc().orElseThrow(RuntimeException::new);
        return entity.getUrl();
    }

    //TODO; upload to S3
    public void create(MultipartFile frame) {

    }
}

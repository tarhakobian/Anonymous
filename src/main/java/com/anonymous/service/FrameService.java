package com.anonymous.service;

import com.anonymous.util.Mapper;
import com.anonymous.exception.FrameNotFoundException;
import com.anonymous.model.dto.responce.FrameResponse;
import com.anonymous.model.entity.FrameEntity;
import com.anonymous.repository.FrameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Service class for managing frames in the system.
 * <p>
 * Author: Taron Hakobyan
 */
@Service
@RequiredArgsConstructor
public class FrameService {

    private static final String S3_DIRECTORY = "frames";

    private final FrameRepository repository;
    private final S3StorageService storageService;

    /**
     * Retrieves all frames.
     *
     * @param pageNumber The page number.
     * @param size       The number of frames per page.
     * @return List of {@link FrameResponse} objects.
     */
    public List<FrameResponse> getAll(Integer pageNumber, Integer size) {
        return repository.findAll(PageRequest.of(pageNumber, size)).stream()
                .map(Mapper::mapFrame)
                .toList();
    }

    /**
     * Creates a new frame from the provided file.
     *
     * @param frame The MultipartFile representing the frame to be created.
     * @return The identifier of the newly created frame.
     * @throws RuntimeException if the upload fails.
     */
    public Integer create(MultipartFile frame) {
        String url = storageService.upload(frame, S3_DIRECTORY);
        if (url == null) {
            throw new RuntimeException("Failed to upload frame");
        }

        FrameEntity entity = new FrameEntity();
        entity.setUrl(url);
        repository.save(entity);

        return entity.getId();
    }

    /**
     * Deletes a frame by its identifier.
     *
     * @param frameId The identifier of the frame to be deleted.
     * @throws FrameNotFoundException if the frame is not found.
     */
    public void delete(Integer frameId) {
        FrameEntity entity = repository.findById(frameId)
                .orElseThrow(FrameNotFoundException::new);

        storageService.delete(entity.getUrl());

        repository.delete(entity);
    }
}

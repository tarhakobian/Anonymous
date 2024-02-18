package com.anonymous.controller;

import com.anonymous.model.dto.responce.FrameResponse;
import com.anonymous.service.FrameService;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Controller class for managing frames in anonymous posts.
 * <p>
 * Author: Taron Hakobyan
 */
@RestController
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class FrameController {

    private final FrameService service;

    /**
     * Retrieves all frames with pagination support.
     *
     * @param pageNumber Page number (default: 0).
     * @param size       Number of items per page (default: 10).
     * @return ResponseEntity with a list of FrameResponse objects.
     */
    @GetMapping("/anonymous-posts/frames")
    public ResponseEntity<?> getAllFrames(@RequestParam(defaultValue = "0") Integer pageNumber,
                                          @RequestParam(defaultValue = "10") Integer size) {
        List<FrameResponse> frameResponses = service.getAll(pageNumber, size);
        return ResponseEntity.ok(frameResponses);
    }

    /**
     * Creates a new frame for an anonymous post.
     *
     * @param frame Multipart file representing the frame content.
     * @return ResponseEntity with the ID of the created frame.
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(value = "/anonymous-posts/frames", consumes = "multipart/form-data")
    public ResponseEntity<?> create(MultipartFile frame) {
        Integer frameId = service.create(frame);
        return new ResponseEntity<>(frameId, HttpStatus.CREATED);
    }

    /**
     * Deletes a frame by its identifier.
     *
     * @param frameId Identifier of the frame to be deleted.
     * @return ResponseEntity with the ID of the deleted frame.
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping(value = "/anonymous-posts/frames/{frameId}")
    public ResponseEntity<?> delete(@PathVariable @Positive Integer frameId) {
        service.delete(frameId);
        return new ResponseEntity<>(frameId, HttpStatus.OK);
    }
}

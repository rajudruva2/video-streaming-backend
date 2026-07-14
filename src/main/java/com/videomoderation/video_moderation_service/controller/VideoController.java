package com.videomoderation.video_moderation_service.controller;

import com.videomoderation.video_moderation_service.dto.UploadResponse;
import com.videomoderation.video_moderation_service.service.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/videos")
@RequiredArgsConstructor
public class VideoController {
    private final VideoService videoService;

    @PostMapping(value = "upload", consumes = "multipart/form-data")
    public ResponseEntity<UploadResponse> uploadVideo(
            @RequestParam("file") MultipartFile file) {

        UploadResponse response = videoService.uploadVideo(file);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }
}

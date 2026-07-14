package com.videomoderation.video_moderation_service.controller;

import com.videomoderation.video_moderation_service.dto.UploadResponse;
import com.videomoderation.video_moderation_service.dto.VideoResponse;
import com.videomoderation.video_moderation_service.service.VideoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/videos")
@RequiredArgsConstructor
@Tag(name = "Video API", description = "Video upload and moderation APIs")
public class VideoController {

    private final VideoService videoService;

    @Operation(
            summary = "Upload a video",
            description = "Uploads a video and starts asynchronous moderation."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Video uploaded successfully"),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })

    @PostMapping(value = "upload", consumes = "multipart/form-data")
    public ResponseEntity<UploadResponse> uploadVideo(
            @RequestParam("file") MultipartFile file) {

        UploadResponse response = videoService.uploadVideo(file);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    @Operation(
            summary = "Get video details",
            description = "Returns the current moderation status of a video."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Video found"),
            @ApiResponse(responseCode = "404", description = "Video not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })

    @GetMapping("getVideo/{id}")
    public ResponseEntity<VideoResponse> getVideo(
            @PathVariable UUID id
    ) {
        return ResponseEntity.ok(videoService.getVideo(id));
    }
}

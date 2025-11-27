package com.rentvideo.controller;

import com.rentvideo.dto.request.VideoRequest;
import com.rentvideo.dto.response.ApiResponse;
import com.rentvideo.dto.response.VideoResponse;
import com.rentvideo.service.VideoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/videos")
@RequiredArgsConstructor
public class VideoController {
    
    private final VideoService videoService;
    
    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<VideoResponse>> getAllVideos(
            @RequestParam(required = false) Boolean availableOnly,
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) String title
    ) {
        List<VideoResponse> videos;
        
        if (title != null && !title.isEmpty()) {
            videos = videoService.searchVideosByTitle(title);
        } else if (genre != null && !genre.isEmpty()) {
            videos = videoService.getVideosByGenre(genre);
        } else if (Boolean.TRUE.equals(availableOnly)) {
            videos = videoService.getAvailableVideos();
        } else {
            videos = videoService.getAllVideos();
        }
        
        return ResponseEntity.ok(videos);
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<VideoResponse> getVideoById(@PathVariable Long id) {
        VideoResponse video = videoService.getVideoById(id);
        return ResponseEntity.ok(video);
    }
    
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<VideoResponse> createVideo(@Valid @RequestBody VideoRequest request) {
        VideoResponse video = videoService.createVideo(request);
        return new ResponseEntity<>(video, HttpStatus.CREATED);
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<VideoResponse> updateVideo(
            @PathVariable Long id,
            @Valid @RequestBody VideoRequest request
    ) {
        VideoResponse video = videoService.updateVideo(id, request);
        return ResponseEntity.ok(video);
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> deleteVideo(@PathVariable Long id) {
        videoService.deleteVideo(id);
        return ResponseEntity.ok(new ApiResponse("Video deleted successfully"));
    }
}

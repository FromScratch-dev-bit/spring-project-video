package com.rentvideo.service;

import com.rentvideo.dto.request.VideoRequest;
import com.rentvideo.dto.response.VideoResponse;
import com.rentvideo.exception.ResourceNotFoundException;
import com.rentvideo.model.Video;
import com.rentvideo.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VideoService {
    
    private final VideoRepository videoRepository;
    
    @Transactional
    public VideoResponse createVideo(VideoRequest request) {
        Video video = new Video();
        video.setTitle(request.getTitle());
        video.setDescription(request.getDescription());
        video.setDirector(request.getDirector());
        video.setGenre(request.getGenre());
        video.setReleaseYear(request.getReleaseYear());
        video.setDurationMinutes(request.getDurationMinutes());
        video.setRentalPricePerDay(request.getRentalPricePerDay());
        video.setTotalCopies(request.getTotalCopies());
        video.setAvailableCopies(request.getTotalCopies());
        video.setCoverImageUrl(request.getCoverImageUrl());
        video.setAvailable(true);
        
        Video savedVideo = videoRepository.save(video);
        return mapToVideoResponse(savedVideo);
    }
    
    public List<VideoResponse> getAllVideos() {
        return videoRepository.findAll().stream()
                .map(this::mapToVideoResponse)
                .collect(Collectors.toList());
    }
    
    public List<VideoResponse> getAvailableVideos() {
        return videoRepository.findByAvailableTrue().stream()
                .map(this::mapToVideoResponse)
                .collect(Collectors.toList());
    }
    
    public VideoResponse getVideoById(Long id) {
        Video video = videoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Video not found with id: " + id));
        return mapToVideoResponse(video);
    }
    
    public Video getVideoEntityById(Long id) {
        return videoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Video not found with id: " + id));
    }
    
    @Transactional
    public VideoResponse updateVideo(Long id, VideoRequest request) {
        Video video = videoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Video not found with id: " + id));
        
        video.setTitle(request.getTitle());
        video.setDescription(request.getDescription());
        video.setDirector(request.getDirector());
        video.setGenre(request.getGenre());
        video.setReleaseYear(request.getReleaseYear());
        video.setDurationMinutes(request.getDurationMinutes());
        video.setRentalPricePerDay(request.getRentalPricePerDay());
        
        // Update copies carefully
        int copyDifference = request.getTotalCopies() - video.getTotalCopies();
        video.setTotalCopies(request.getTotalCopies());
        video.setAvailableCopies(video.getAvailableCopies() + copyDifference);
        
        if (video.getAvailableCopies() > 0) {
            video.setAvailable(true);
        }
        
        video.setCoverImageUrl(request.getCoverImageUrl());
        
        Video updatedVideo = videoRepository.save(video);
        return mapToVideoResponse(updatedVideo);
    }
    
    @Transactional
    public void deleteVideo(Long id) {
        Video video = videoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Video not found with id: " + id));
        videoRepository.delete(video);
    }
    
    public List<VideoResponse> searchVideosByTitle(String title) {
        return videoRepository.findByTitleContainingIgnoreCase(title).stream()
                .map(this::mapToVideoResponse)
                .collect(Collectors.toList());
    }
    
    public List<VideoResponse> getVideosByGenre(String genre) {
        return videoRepository.findByGenre(genre).stream()
                .map(this::mapToVideoResponse)
                .collect(Collectors.toList());
    }
    
    private VideoResponse mapToVideoResponse(Video video) {
        return VideoResponse.builder()
                .id(video.getId())
                .title(video.getTitle())
                .description(video.getDescription())
                .director(video.getDirector())
                .genre(video.getGenre())
                .releaseYear(video.getReleaseYear())
                .durationMinutes(video.getDurationMinutes())
                .rentalPricePerDay(video.getRentalPricePerDay())
                .totalCopies(video.getTotalCopies())
                .availableCopies(video.getAvailableCopies())
                .coverImageUrl(video.getCoverImageUrl())
                .available(video.getAvailable())
                .createdAt(video.getCreatedAt())
                .build();
    }
}

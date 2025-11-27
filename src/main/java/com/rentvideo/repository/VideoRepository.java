package com.rentvideo.repository;

import com.rentvideo.model.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VideoRepository extends JpaRepository<Video, Long> {
    
    List<Video> findByAvailableTrue();
    
    List<Video> findByGenre(String genre);
    
    List<Video> findByTitleContainingIgnoreCase(String title);
    
    List<Video> findByReleaseYear(Integer releaseYear);
}

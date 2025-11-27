package com.rentvideo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "videos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Video {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String title;
    
    @Column(length = 1000)
    private String description;
    
    private String director;
    
    private String genre;
    
    @Column(nullable = false)
    private Integer releaseYear;
    
    @Column(nullable = false)
    private Integer durationMinutes;
    
    @Column(nullable = false)
    private BigDecimal rentalPricePerDay;
    
    @Column(nullable = false)
    private Integer totalCopies = 1;
    
    @Column(nullable = false)
    private Integer availableCopies = 1;
    
    private String coverImageUrl;
    
    @Column(nullable = false)
    private Boolean available = true;
    
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;
    
    public void decreaseAvailableCopies() {
        if (this.availableCopies > 0) {
            this.availableCopies--;
        }
        if (this.availableCopies == 0) {
            this.available = false;
        }
    }
    
    public void increaseAvailableCopies() {
        if (this.availableCopies < this.totalCopies) {
            this.availableCopies++;
        }
        if (this.availableCopies > 0) {
            this.available = true;
        }
    }
}

package com.rentvideo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VideoResponse {
    
    private Long id;
    private String title;
    private String description;
    private String director;
    private String genre;
    private Integer releaseYear;
    private Integer durationMinutes;
    private BigDecimal rentalPricePerDay;
    private Integer totalCopies;
    private Integer availableCopies;
    private String coverImageUrl;
    private Boolean available;
    private LocalDateTime createdAt;
}

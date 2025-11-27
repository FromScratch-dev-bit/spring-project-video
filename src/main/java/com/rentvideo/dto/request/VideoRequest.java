package com.rentvideo.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class VideoRequest {
    
    @NotBlank(message = "Title is required")
    private String title;
    
    private String description;
    
    private String director;
    
    private String genre;
    
    @NotNull(message = "Release year is required")
    @Min(value = 1900, message = "Release year must be 1900 or later")
    private Integer releaseYear;
    
    @NotNull(message = "Duration is required")
    @Min(value = 1, message = "Duration must be at least 1 minute")
    private Integer durationMinutes;
    
    @NotNull(message = "Rental price is required")
    @Min(value = 0, message = "Rental price must be positive")
    private BigDecimal rentalPricePerDay;
    
    @NotNull(message = "Total copies is required")
    @Min(value = 1, message = "Total copies must be at least 1")
    private Integer totalCopies;
    
    private String coverImageUrl;
}

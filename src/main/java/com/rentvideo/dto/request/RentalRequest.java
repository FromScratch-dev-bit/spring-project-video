package com.rentvideo.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RentalRequest {
    
    @NotNull(message = "Video ID is required")
    private Long videoId;
    
    @NotNull(message = "Rental days is required")
    @Min(value = 1, message = "Rental days must be at least 1")
    private Integer rentalDays;
}

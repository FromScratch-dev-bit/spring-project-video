package com.rentvideo.dto.response;

import com.rentvideo.model.Rental;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RentalResponse {
    
    private Long id;
    private Long userId;
    private String username;
    private Long videoId;
    private String videoTitle;
    private LocalDate rentalDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private BigDecimal rentalPrice;
    private BigDecimal lateFee;
    private BigDecimal totalAmount;
    private Rental.RentalStatus status;
    private LocalDateTime createdAt;
}

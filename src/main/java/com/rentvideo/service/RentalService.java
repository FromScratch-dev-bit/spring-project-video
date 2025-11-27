package com.rentvideo.service;

import com.rentvideo.dto.request.RentalRequest;
import com.rentvideo.dto.response.RentalResponse;
import com.rentvideo.exception.BadRequestException;
import com.rentvideo.exception.ResourceNotFoundException;
import com.rentvideo.exception.VideoNotAvailableException;
import com.rentvideo.model.Rental;
import com.rentvideo.model.User;
import com.rentvideo.model.Video;
import com.rentvideo.repository.RentalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RentalService {
    
    private final RentalRepository rentalRepository;
    private final VideoService videoService;
    private final UserService userService;
    
    @Transactional
    public RentalResponse rentVideo(RentalRequest request) {
        String username = userService.getCurrentUsername();
        User user = userService.getUserByUsername(username);
        
        Video video = videoService.getVideoEntityById(request.getVideoId());
        
        if (!video.getAvailable() || video.getAvailableCopies() <= 0) {
            throw new VideoNotAvailableException("Video is not available for rental");
        }
        
        // Decrease available copies
        video.decreaseAvailableCopies();
        
        // Create rental
        Rental rental = new Rental();
        rental.setUser(user);
        rental.setVideo(video);
        rental.setRentalDate(LocalDate.now());
        rental.setDueDate(LocalDate.now().plusDays(request.getRentalDays()));
        
        BigDecimal rentalPrice = video.getRentalPricePerDay()
                .multiply(BigDecimal.valueOf(request.getRentalDays()));
        rental.setRentalPrice(rentalPrice);
        rental.setTotalAmount(rentalPrice);
        rental.setStatus(Rental.RentalStatus.ACTIVE);
        
        Rental savedRental = rentalRepository.save(rental);
        return mapToRentalResponse(savedRental);
    }
    
    @Transactional
    public RentalResponse returnVideo(Long rentalId) {
        String username = userService.getCurrentUsername();
        User user = userService.getUserByUsername(username);
        
        Rental rental = rentalRepository.findById(rentalId)
                .orElseThrow(() -> new ResourceNotFoundException("Rental not found with id: " + rentalId));
        
        // Check if the rental belongs to the current user (unless admin)
        if (!rental.getUser().getId().equals(user.getId()) && 
            !user.getRole().name().equals("ADMIN")) {
            throw new BadRequestException("You can only return your own rentals");
        }
        
        if (rental.getStatus() == Rental.RentalStatus.RETURNED) {
            throw new BadRequestException("Video has already been returned");
        }
        
        LocalDate returnDate = LocalDate.now();
        rental.setReturnDate(returnDate);
        rental.setStatus(Rental.RentalStatus.RETURNED);
        
        // Calculate late fee if overdue
        if (returnDate.isAfter(rental.getDueDate())) {
            long daysLate = ChronoUnit.DAYS.between(rental.getDueDate(), returnDate);
            BigDecimal lateFeePerDay = rental.getVideo().getRentalPricePerDay()
                    .multiply(BigDecimal.valueOf(0.5)); // 50% of daily rate as late fee
            BigDecimal lateFee = lateFeePerDay.multiply(BigDecimal.valueOf(daysLate));
            rental.setLateFee(lateFee);
            rental.setTotalAmount(rental.getRentalPrice().add(lateFee));
        }
        
        // Increase available copies
        Video video = rental.getVideo();
        video.increaseAvailableCopies();
        
        Rental updatedRental = rentalRepository.save(rental);
        return mapToRentalResponse(updatedRental);
    }
    
    public List<RentalResponse> getAllRentals() {
        return rentalRepository.findAll().stream()
                .map(this::mapToRentalResponse)
                .collect(Collectors.toList());
    }
    
    public List<RentalResponse> getCurrentUserRentals() {
        String username = userService.getCurrentUsername();
        User user = userService.getUserByUsername(username);
        
        return rentalRepository.findByUser(user).stream()
                .map(this::mapToRentalResponse)
                .collect(Collectors.toList());
    }
    
    public List<RentalResponse> getActiveRentals() {
        return rentalRepository.findByStatus(Rental.RentalStatus.ACTIVE).stream()
                .map(this::mapToRentalResponse)
                .collect(Collectors.toList());
    }
    
    private RentalResponse mapToRentalResponse(Rental rental) {
        return RentalResponse.builder()
                .id(rental.getId())
                .userId(rental.getUser().getId())
                .username(rental.getUser().getUsername())
                .videoId(rental.getVideo().getId())
                .videoTitle(rental.getVideo().getTitle())
                .rentalDate(rental.getRentalDate())
                .dueDate(rental.getDueDate())
                .returnDate(rental.getReturnDate())
                .rentalPrice(rental.getRentalPrice())
                .lateFee(rental.getLateFee())
                .totalAmount(rental.getTotalAmount())
                .status(rental.getStatus())
                .createdAt(rental.getCreatedAt())
                .build();
    }
}

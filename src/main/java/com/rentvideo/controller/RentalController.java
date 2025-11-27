package com.rentvideo.controller;

import com.rentvideo.dto.request.RentalRequest;
import com.rentvideo.dto.response.RentalResponse;
import com.rentvideo.service.RentalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rentals")
@RequiredArgsConstructor
public class RentalController {
    
    private final RentalService rentalService;
    
    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<RentalResponse> rentVideo(@Valid @RequestBody RentalRequest request) {
        RentalResponse rental = rentalService.rentVideo(request);
        return new ResponseEntity<>(rental, HttpStatus.CREATED);
    }
    
    @GetMapping("/my-rentals")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<RentalResponse>> getCurrentUserRentals() {
        List<RentalResponse> rentals = rentalService.getCurrentUserRentals();
        return ResponseEntity.ok(rentals);
    }
    
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<RentalResponse>> getAllRentals(
            @RequestParam(required = false) Boolean activeOnly
    ) {
        List<RentalResponse> rentals;
        
        if (Boolean.TRUE.equals(activeOnly)) {
            rentals = rentalService.getActiveRentals();
        } else {
            rentals = rentalService.getAllRentals();
        }
        
        return ResponseEntity.ok(rentals);
    }
    
    @PutMapping("/{id}/return")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<RentalResponse> returnVideo(@PathVariable Long id) {
        RentalResponse rental = rentalService.returnVideo(id);
        return ResponseEntity.ok(rental);
    }
}

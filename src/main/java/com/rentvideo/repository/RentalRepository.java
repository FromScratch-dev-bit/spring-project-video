package com.rentvideo.repository;

import com.rentvideo.model.Rental;
import com.rentvideo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RentalRepository extends JpaRepository<Rental, Long> {
    
    List<Rental> findByUser(User user);
    
    List<Rental> findByUserId(Long userId);
    
    List<Rental> findByStatus(Rental.RentalStatus status);
    
    List<Rental> findByUserAndStatus(User user, Rental.RentalStatus status);
}

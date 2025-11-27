package com.rentvideo.service;

import com.rentvideo.model.User;
import com.rentvideo.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {
    
    @Mock
    private UserRepository userRepository;
    
    @InjectMocks
    private CustomUserDetailsService userDetailsService;
    
    @Test
    void loadUserByUsername_Success() {
        // Arrange
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password");
        user.setActive(true);
        
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        
        // Act
        UserDetails userDetails = userDetailsService.loadUserByUsername("testuser");
        
        // Assert
        assertNotNull(userDetails);
        assertEquals("testuser", userDetails.getUsername());
        assertTrue(userDetails.isEnabled());
        verify(userRepository, times(1)).findByUsername("testuser");
    }
    
    @Test
    void loadUserByUsername_UserNotFound() {
        // Arrange
        when(userRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());
        
        // Act & Assert
        assertThrows(UsernameNotFoundException.class, 
            () -> userDetailsService.loadUserByUsername("nonexistent"));
        verify(userRepository, times(1)).findByUsername("nonexistent");
    }
}

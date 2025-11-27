package com.rentvideo.dto.response;

import com.rentvideo.model.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    
    private Long id;
    private String username;
    private String fullName;
    private String email;
    private String phoneNumber;
    private Role role;
    private Boolean active;
    private LocalDateTime createdAt;
}

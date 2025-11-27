package com.rentvideo.dto.request;

import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class UpdateUserRequest {
    
    private String fullName;
    
    @Email(message = "Email should be valid")
    private String email;
    
    private String phoneNumber;
}

package com.rentvideo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtAuthResponse {
    
    private String accessToken;
    private String tokenType;
    private UserResponse user;
    private String message;
}

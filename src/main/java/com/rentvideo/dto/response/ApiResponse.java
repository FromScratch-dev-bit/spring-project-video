package com.rentvideo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiResponse {
    
    private String message;
    private Object data;
    
    public ApiResponse(String message) {
        this.message = message;
    }
}

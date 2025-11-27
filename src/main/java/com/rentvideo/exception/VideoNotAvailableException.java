package com.rentvideo.exception;

public class VideoNotAvailableException extends RuntimeException {
    
    public VideoNotAvailableException(String message) {
        super(message);
    }
}

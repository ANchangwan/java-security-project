package com.example.security_project.exception;

public class CustomJWTException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public CustomJWTException(String message) {
        super(message);
    }

    public CustomJWTException(String message, Throwable cause) {
        super(message, cause);
    }

    public CustomJWTException(Throwable cause) {
        super(cause);
    }
}

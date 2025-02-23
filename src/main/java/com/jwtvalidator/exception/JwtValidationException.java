package com.jwtvalidator.exception;

public class JwtValidationException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public JwtValidationException() {
        super();
    }

    public JwtValidationException(String message) {
        super(message);
    }

    public JwtValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public JwtValidationException(Throwable cause) {
        super(cause);
    }
}

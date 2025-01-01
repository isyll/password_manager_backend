package com.isyll.password_manager.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class InvalidTokenException extends RuntimeException {

    public InvalidTokenException(String message) {
        super(message);
    }
}

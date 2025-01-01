package com.isyll.password_manager.exceptions;

public class InactiveUserActionException extends RuntimeException {

    public InactiveUserActionException(String message) {
        super(message);
    }
}

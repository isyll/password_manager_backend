package com.isyll.password_manager.exceptions;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UniqueConstraintViolationException extends RuntimeException {

    private final Map<String, String> errors;

}

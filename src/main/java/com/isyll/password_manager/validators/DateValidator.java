package com.isyll.password_manager.validators;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DateValidator implements ConstraintValidator<DateValidation, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        try {
            ZonedDateTime date = ZonedDateTime.parse(value, DateTimeFormatter.ISO_ZONED_DATE_TIME);
            return date.isBefore(ZonedDateTime.now());
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}
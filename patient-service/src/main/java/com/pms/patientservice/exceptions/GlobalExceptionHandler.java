package com.pms.patientservice.exceptions;

import static com.pms.patientservice.commons.PatientServiceCommonsConstants.EMAIL_ALREADY_EXISTS;
import static com.pms.patientservice.commons.PatientServiceCommonsConstants.MESSAGE_TEXT;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
//ControllerAdvice is a meta-annotation that indicates that the class is an advice class, and it lets us handle cross-cutting concerns such as exception handling, on both sides of controllers and services.
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> handleEmailAlreadyExistsException(EmailAlreadyExistsException ex) {
        logger.warn("Email address already exists {}", ex.getMessage());
        Map<String, String> errors = new HashMap<>();
        errors.put(MESSAGE_TEXT, EMAIL_ALREADY_EXISTS);
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(PatientNotFoundException.class)
    public ResponseEntity<Map<String, String>> handlePatientNotFoundException(PatientNotFoundException ex) {
        logger.warn("Patient not found {}", ex.getMessage());
        Map<String, String> errors = new HashMap<>();
        errors.put(MESSAGE_TEXT, "Patient not found");
        return ResponseEntity.badRequest().body(errors);
    }
}

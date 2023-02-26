package ru.practicum.shareit.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    public ResponseEntity<List<String>> handleIncorrectParameterException(IncorrectParameterException e) {
        log.warn(e.getMessage());
        List<String> errors = new ArrayList<>();
        errors.add(e.getMessage());
        return new ResponseEntity<>(errors, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<List<String>> handleUserNotFoundException(UserNotFoundException e) {
        log.warn(e.getMessage());
        List<String> errors = new ArrayList<>();
        errors.add(e.getMessage());
        return new ResponseEntity<>(errors, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<List<String>> handleItemNotFoundException(ItemNotFoundException e) {
        log.warn(e.getMessage());
        List<String> errors = new ArrayList<>();
        errors.add(e.getMessage());
        return new ResponseEntity<>(errors, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<List<String>> handleBookingNotFoundException(BookingNotFoundException e) {
        log.warn(e.getMessage());
        List<String> errors = new ArrayList<>();
        errors.add(e.getMessage());
        return new ResponseEntity<>(errors, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<List<String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.warn(e.getMessage());
        List<String> errors = new ArrayList<>();
        errors.add(e.getMessage());
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<List<String>> handleConstraintViolationException(ConstraintViolationException e) {
        log.warn(e.getMessage());
        List<String> errors = new ArrayList<>();
        errors.add(e.getMessage());
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<List<String>> handleBadParameterException(BadParameterException e) {
        log.warn(e.getMessage());
        List<String> errors = new ArrayList<>();
        errors.add(e.getMessage());
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity handleThrowableException(Throwable e) {
        log.warn(e.getMessage());
        Map<String, String> errors = new HashMap<>();
        errors.put("error", e.getMessage());
        return new ResponseEntity(errors, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler
    public ResponseEntity handleValidationException(ValidationException e) {
        log.warn(e.getMessage());
        List<String> errors = new ArrayList<>();
        errors.add(e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
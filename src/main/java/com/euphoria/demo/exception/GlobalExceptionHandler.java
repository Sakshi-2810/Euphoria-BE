package com.euphoria.demo.exception;

import com.euphoria.demo.dto.Response;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // Handle validation errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Response> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, Object> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        Response response = new Response("Validation Failed", errors);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // Handle all other exceptions (fallback)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response> handleGlobalException(Exception ex) {
        log.error("An unexpected error occurred: ", ex);
        return new ResponseEntity<>(new Response(ex.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(CustomDataException.class)
    public ResponseEntity<Response> handleCustomDataException(CustomDataException ex) {
        return new ResponseEntity<>(new Response(ex.getMessage(), null), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<Response> handleExpiredJwtException(ExpiredJwtException ex) {
        return new ResponseEntity<>(new Response("Token has expired", null), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<Response> handleNoResourceFoundException(NoResourceFoundException ex) {
        return new ResponseEntity<>(new Response("Resource not found", null), HttpStatus.NOT_FOUND);
    }
}

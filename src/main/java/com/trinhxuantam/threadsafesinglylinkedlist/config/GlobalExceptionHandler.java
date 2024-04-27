package com.trinhxuantam.threadsafesinglylinkedlist.config;

import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Global exception handler for catching and handling exceptions across the
 * entire application.
 * It intercepts exceptions thrown from any controller and provides a standard
 * response.
 */
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(NoSuchElementException.class)
    protected ResponseEntity<ErrorResponse> handleNoSuchElementException(NoSuchElementException ex) {
        return new ResponseEntity<>(new ErrorResponse("Element not found", ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalStateException.class)
    protected ResponseEntity<ErrorResponse> handleIllegalStateException(IllegalStateException ex) {
        return new ResponseEntity<>(new ErrorResponse("Illegal state", ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse> handleGeneralException(Exception ex) {
        return new ResponseEntity<>(new ErrorResponse("Internal server error", ex.getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * A simple error response object that contains a title and a detail.
     */
    @AllArgsConstructor
    static class ErrorResponse {
        @Getter
        @Setter
        private String title; // Error title

        @Getter
        @Setter
        private String detail; // Error detail
    }
}

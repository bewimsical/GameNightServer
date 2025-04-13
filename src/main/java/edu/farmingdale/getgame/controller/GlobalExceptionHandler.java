package edu.farmingdale.getgame.controller;

import edu.farmingdale.getgame.exception.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    ResponseEntity<Void> handleNotFoundException(ResourceNotFoundException ex) {
        return ResponseEntity.badRequest()
                .build();
    }
}

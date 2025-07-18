package com.example.spring_6_mvc.controllers;

import com.example.spring_6_mvc.exceptions.NotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Exceptional handler with Controller advice layer helps handle mentioned exceptions at time of all api calls
 */
@ControllerAdvice
public class ExceptionController {
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Map<String, String>> handleNotFoundException(NotFoundException ex){
        System.out.println("In global exception handler");
        Map<String, String> body = new HashMap<>();
        body.put("error", "Entity Not found");
        body.put("message", ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<Map<String, String>>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex){
//        Map<String, String> body = new HashMap<>();
//        body.put("error", ex.getMessage());

        //Custom error message
        List<Map<String, String>> response = ex.getFieldErrors()
                .stream()
                .map(fieldError -> {
                    Map<String, String> errorMap = new HashMap<>();
                    errorMap.put(fieldError.getField(), fieldError.getDefaultMessage());
                    return errorMap;
                })
                .collect(Collectors.toList());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TransactionSystemException.class)
    public ResponseEntity handleTxnSystemException(TransactionSystemException ex) {
        //we will handle this if this was thrown due to constraint violation exception ie bcz of db level
        // validations we apply like column length

        if(ex.getCause().getCause() instanceof ConstraintViolationException){
            ConstraintViolationException cve = (ConstraintViolationException) ex.getCause().getCause();

            List<Map<String, String>> response = cve.getConstraintViolations()
                    .stream()
                    .map(constraintViolation -> {
                        Map<String, String> mpp = new HashMap<>();
                        mpp.put(constraintViolation.getPropertyPath().toString(), constraintViolation.getMessage());
                        return mpp;
                    })
                    .collect(Collectors.toList());

            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}

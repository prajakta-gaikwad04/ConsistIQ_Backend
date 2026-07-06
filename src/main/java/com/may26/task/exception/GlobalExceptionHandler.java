package com.may26.task.exception;

import java.util.HashMap;

import java.util.Map;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.may26.exception.TaskNotFoundException;
import com.may26.exception.UserNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationException(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult()
          .getFieldErrors()
          .forEach(error ->
              errors.put(
                  error.getField(),
                  error.getDefaultMessage()
              )
          );

        return errors;
    }
    
    @ExceptionHandler(UserNotFoundException.class)
    public Map<String,String> handleUserNotFound(UserNotFoundException ex){
    	
    	Map<String,String> error=new HashMap<>();
    	error.put("error", ex.getMessage());
    	return error;
    }
    
    
    @ExceptionHandler(TaskNotFoundException.class)
    public Map<String,String> handleTaskNotFound(TaskNotFoundException ex){
    	Map<String,String> error=new HashMap<>();
    	error.put("error", ex.getMessage());
    	return error;
    	}
    }
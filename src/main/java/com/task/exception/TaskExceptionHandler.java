package com.task.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class TaskExceptionHandler {
    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<TaskError> handleTaskNotFoundException(TaskNotFoundException ex) {
        TaskError error = new TaskError();
        error.setStatusCode(HttpStatus.NOT_MODIFIED.toString());
        error.setErrorMessage(ex.getMessage());
        return new ResponseEntity<>(error,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TaskValidationException.class)
    public ResponseEntity<TaskError> invalidTaskException(TaskValidationException ex) {
        TaskError error = new TaskError();
        error.setStatusCode(HttpStatus.NOT_MODIFIED.toString());
        error.setErrorMessage(ex.getMessage());
        return new ResponseEntity<>(error,HttpStatus.NOT_MODIFIED);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<TaskError>> handleValidationException(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();

        List<TaskError> errorList = new ArrayList<>();
        result.getFieldErrors().forEach(fieldError -> {
            TaskError error = new TaskError();
            error.setStatusCode(fieldError.getField());
            error.setErrorMessage(fieldError.getDefaultMessage());
            errorList.add(error);
        });
        return new ResponseEntity<>(errorList, HttpStatus.BAD_REQUEST);
    }
}

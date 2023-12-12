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

    /**
     * Handles {@link TaskNotFoundException}.
     * @param ex The exception instance.
     * @return ResponseEntity containing the error details.
     */
    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<TaskError> handleTaskNotFoundException(TaskNotFoundException ex) {
        TaskError error = new TaskError("TASK_ERROR", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    /**
     * Handles {@link TaskValidationException}.
     * @param ex The exception instance.
     * @return ResponseEntity containing the error details.
     */
    @ExceptionHandler(TaskValidationException.class)
    public ResponseEntity<TaskError> invalidTaskException(TaskValidationException ex) {
        TaskError error = new TaskError("MODIFICATION_ERROR", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_MODIFIED);
    }

    /**
     * Handles {@link MethodArgumentNotValidException}.
     * @param ex The exception instance.
     * @return ResponseEntity containing a list of validation errors.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<TaskError>> handleValidationException(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();

        List<TaskError> errorList = new ArrayList<>();
        result.getFieldErrors().forEach(fieldError -> {
            TaskError error = new TaskError(fieldError.getField(), fieldError.getDefaultMessage());
            errorList.add(error);
        });
        return new ResponseEntity<>(errorList, HttpStatus.BAD_REQUEST);
    }
}

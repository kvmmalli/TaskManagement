package com.task.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Custom exception to indicate that a Task was not found.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class TaskNotFoundException extends RuntimeException {

    /**
     * Constructs a new TaskNotFoundException with the specified detail message.
     * @param message the detail message.
     */
    public TaskNotFoundException(String message) {
        super(message);
    }
}

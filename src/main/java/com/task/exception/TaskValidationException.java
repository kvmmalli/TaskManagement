package com.task.exception;


/**
 * Custom exception to indicate that a Task was invalid.
 */
public class TaskValidationException extends RuntimeException {

    /**
     * Constructs a new TaskValidationException with the specified detail message.
     * @param message the detail message.
     */
    public TaskValidationException(String message) {
        super(message);
    }
}

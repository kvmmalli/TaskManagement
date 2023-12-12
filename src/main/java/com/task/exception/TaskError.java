package com.task.exception;

import lombok.Data;

@Data
public class TaskError {
    private String statusCode;
    private String errorMessage;
}

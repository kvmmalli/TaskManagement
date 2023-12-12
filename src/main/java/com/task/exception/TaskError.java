package com.task.exception;

import lombok.Data;

@Data
public class TaskError {
    private String errorCode;
    private String errorMessage;

    TaskError(String errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

}

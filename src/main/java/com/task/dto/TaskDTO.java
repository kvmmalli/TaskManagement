package com.task.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class TaskDTO {
    private String projectName;
    private Long taskId;
    @NotNull(message = "Title is required")
    @Size(max = 100, message = "Title cannot exceed 100 characters")
    private String title;

    @Size(max = 255, message = "Description cannot exceed 255 characters")
    private String description;

    @NotNull(message = "Status is required")
    @Pattern(regexp = "^(In Progress|Pending|Completed)$", message = "Status must be 'In Progress', 'Pending', or 'Completed'")
    @Size(max = 20, message = "Status cannot exceed 20 characters")
    private String status;

    @NotNull(message = "Priority is required")
    @Min(value = 1, message = "Priority must be at least 1")
    private Integer priority;

    @FutureOrPresent(message = "Due date must be in the present or future")
    private LocalDate dueDate;
}

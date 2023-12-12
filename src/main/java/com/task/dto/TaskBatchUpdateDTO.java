package com.task.dto;

import lombok.Data;
import java.util.List;

@Data
public class TaskBatchUpdateDTO {
    private List<Long> taskIds;
    private String newStatus;
}

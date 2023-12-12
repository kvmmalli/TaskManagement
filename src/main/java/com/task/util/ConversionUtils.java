package com.task.util;

import com.task.dto.TaskDTO;
import com.task.entity.Task;

public class ConversionUtils {

    public static TaskDTO convertToTaskDTO(Task task) {
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setTaskId(task.getId());
        taskDTO.setProjectName(task.getProject().getName());
        taskDTO.setTitle(task.getTitle());
        taskDTO.setDescription(task.getDescription());
        taskDTO.setStatus(task.getStatus());
        taskDTO.setDueDate(task.getDueDate());
        taskDTO.setPriority(task.getPriority());
        return taskDTO;
    }

    public static Task convertToTask(TaskDTO taskDTO) {
        Task task = new Task();
        task.setTitle(taskDTO.getTitle());
        task.setDescription(taskDTO.getDescription());
        task.setStatus(taskDTO.getStatus());
        task.setDueDate(taskDTO.getDueDate());
        task.setPriority(taskDTO.getPriority());
        return task;
    }
}

package com.task.service;

import com.task.dto.TaskDTO;
import com.task.entity.Task;

import java.time.LocalDate;
import java.util.List;

public interface TaskService {
    TaskDTO createTask(Long projectId, TaskDTO task);

    TaskDTO updateTask(Long taskId, TaskDTO updatedTask);

    void deleteTask(Long taskId);

    TaskDTO getTaskById(Long taskId);

    List<TaskDTO> getAllTasks(int page, int size, String sortBy);

    void updateBatchTaskStatus(List<Long> taskIds, String newStatus);
     List<TaskDTO> getOverdueTasks();

    List<TaskDTO> getTasksByProjectId(Long projectId);

    List<TaskDTO> getTasksByProjectIdAndStatus(Long projectId, String status);

    List<TaskDTO> getCompletedTasksAfterEstimatedTime(Long projectId, LocalDate dueDate);



}

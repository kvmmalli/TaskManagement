package com.task.service;

import com.task.dto.TaskDTO;

import java.time.LocalDate;
import java.util.List;

public interface TaskService {
    /**
     * For more information, see {@link TaskServiceImpl#createTask(Long, TaskDTO)}
     */
    TaskDTO createTask(Long projectId, TaskDTO task);

    /**
     * For more information, see {@link TaskServiceImpl#updateTask(Long taskId, TaskDTO updatedTask)}
     */
    TaskDTO updateTask(Long taskId, TaskDTO updatedTask);

    /**
     * For more information, see {@link TaskServiceImpl#deleteTask(Long taskId)}
     */
    void deleteTask(Long taskId);

    /**
     * For more information, see {@link TaskServiceImpl#getTaskById(Long taskId)}
     */
    TaskDTO getTaskById(Long taskId);

    /**
     * For more information, see {@link TaskServiceImpl#getAllTasks(int page, int size, String sortBy)}
     */
    List<TaskDTO> getAllTasks(int page, int size, String sortBy);

    void updateBatchTaskStatus(List<Long> taskIds, String status);

    /**
     * For more information, see {@link TaskServiceImpl#getOverdueTasks()}
     */
    List<TaskDTO> getOverdueTasks();

    /**
     * For more information, see {@link TaskServiceImpl#getTasksByProjectId(Long projectId)}
     */
    List<TaskDTO> getTasksByProjectId(Long projectId);

    /**
     * For more information, see {@link TaskServiceImpl#getTasksByProjectIdAndStatus(Long projectId, String status)}
     */
    List<TaskDTO> getTasksByProjectIdAndStatus(Long projectId, String status);


    /**
     * For more information, see {@link TaskServiceImpl#getCompletedTasksAfterEstimatedTime(Long projectId, LocalDate dueDate)}
     */
    List<TaskDTO> getCompletedTasksAfterEstimatedTime(Long projectId, LocalDate dueDate);



}

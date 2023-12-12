package com.task.controller;
import com.task.dto.TaskBatchUpdateDTO;
import com.task.dto.TaskDTO;
import com.task.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/tasks")
@Validated
public class TaskController {

    @Autowired
    private TaskService taskService;

    /**
     * Retrieve a task by its ID.
     * @param taskId The ID of the task to retrieve.
     * @return ResponseEntity containing the retrieved task.
     */
    @GetMapping("/{taskId}")
    public ResponseEntity<TaskDTO> getTaskById(@PathVariable Long taskId) {
        TaskDTO task = taskService.getTaskById(taskId);
        return new ResponseEntity<>(task, HttpStatus.OK);
    }

    /**
     * Update a task by its ID.
     * @param taskId The ID of the task to update.
     * @param updatedTask The updated task data.
     * @return ResponseEntity containing the updated task.
     */
    @PutMapping("/{taskId}")
    public ResponseEntity<TaskDTO> updateTask(@PathVariable Long taskId, @Valid @RequestBody TaskDTO updatedTask) {
        TaskDTO updatedTaskResult = taskService.updateTask(taskId, updatedTask);
        return new ResponseEntity<>(updatedTaskResult, HttpStatus.OK);
    }

    /**
     * Delete a task by its ID.
     * @param taskId The ID of the task to delete.
     * @return ResponseEntity with a success message.
     */
    @DeleteMapping("/{taskId}")
    public ResponseEntity<String> deleteTask(@PathVariable Long taskId) {
        taskService.deleteTask(taskId);
        return new ResponseEntity<>("Task deleted successfully", HttpStatus.OK);
    }

    /**
     * Retrieve a paginated list of tasks.
     * @param page Page number.
     * @param size Number of tasks per page.
     * @param sortBy Sorting criteria (By default "dueDate").
     * @return ResponseEntity containing the list of tasks.
     */
    @GetMapping
    public ResponseEntity<List<TaskDTO>> getAllTasks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "dueDate") String sortBy) {
        List<TaskDTO> tasks = taskService.getAllTasks(page, size, sortBy);
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    /**
     * To get the tasks which are overdue
     * @return ResponseEntity containing the list of tasks.
     */
    @GetMapping("/over-due")
    public ResponseEntity<List<TaskDTO>> getOverDueTasks() {
        List<TaskDTO> tasks = taskService.getOverdueTasks();
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    /**
     * Update the status of multiple tasks in a batch.
     * @param batchUpdateRequest DTO containing task IDs and the new status.
     * @return ResponseEntity with a success message.
     */
    @PutMapping("/batch-status-updates")
    public ResponseEntity<String> updateBatchStatus(
            @RequestBody TaskBatchUpdateDTO batchUpdateRequest) {
        taskService.updateBatchTaskStatus(batchUpdateRequest.getTaskIds(), batchUpdateRequest.getNewStatus());
        return new ResponseEntity<>("Batch update successful", HttpStatus.OK);
    }

    /**
     * Create a new task for a specific project.
     * @param projectId ID of the project.
     * @param task Task data.
     * @return ResponseEntity containing the created task.
     */
    @PostMapping("/project/{id}")
    public ResponseEntity<TaskDTO> createTask(@PathVariable("id") Long projectId, @Valid  @RequestBody TaskDTO task) {
        TaskDTO createdTask = taskService.createTask(projectId, task);
        return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
    }

    /**
     * Retrieve tasks for a specific project based on status.
     * @param projectId ID of the project.
     * @param status Task status.
     * @return ResponseEntity containing the list of tasks.
     */
    @GetMapping("/project/{id}")
    public ResponseEntity<List<TaskDTO>> getTasksByProjectIdAndStatus(@PathVariable("id") Long projectId, @RequestParam(required = false, value = "status") String status) {
        List<TaskDTO> tasks = null;

        if (status != null) {
            tasks = taskService.getTasksByProjectIdAndStatus(projectId, status);
        } else {
            tasks = taskService.getTasksByProjectId(projectId);
        }
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    /**
     * Retrieve completed tasks for a specific project after a given due date.
     * @param projectId ID of the project.
     * @param dueDate Due date to filter completed tasks.
     * @return ResponseEntity containing the list of completed tasks.
     */
    @GetMapping("/project/{id}/completed-after/{dueDate}")
    public ResponseEntity<List<TaskDTO>> getCompletedTasksAfterEstimatedTime(@PathVariable("id") Long projectId, @PathVariable("dueDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dueDate) {
        List<TaskDTO> tasks = taskService.getCompletedTasksAfterEstimatedTime(projectId, dueDate);
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }
}

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
@RequestMapping("/tasks")
@Validated
public class TaskController {

    @Autowired
    private TaskService taskService;

    @GetMapping("/{taskId}")
    public ResponseEntity<TaskDTO> getTaskById(@PathVariable Long taskId) {
        TaskDTO task = taskService.getTaskById(taskId);
        return new ResponseEntity<>(task, HttpStatus.OK);
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<TaskDTO> updateTask(@PathVariable Long taskId, @Valid @RequestBody TaskDTO updatedTask) {
        TaskDTO updatedTaskResult = taskService.updateTask(taskId, updatedTask);
        return new ResponseEntity<>(updatedTaskResult, HttpStatus.OK);
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<String> deleteTask(@PathVariable Long taskId) {
        taskService.deleteTask(taskId);
        return new ResponseEntity<>("Task deleted successfully", HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<TaskDTO>> getAllTasks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "dueDate") String sortBy
    ) {
        List<TaskDTO> tasks = taskService.getAllTasks(page, size, sortBy);
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    @PutMapping("/batch-status-updates")
    public ResponseEntity<String> updateBatchStatus(
            @RequestBody TaskBatchUpdateDTO batchUpdateRequest) {
        taskService.updateBatchTaskStatus(batchUpdateRequest.getTaskIds(), batchUpdateRequest.getNewStatus());
        return new ResponseEntity<>("Batch update successful", HttpStatus.OK);
    }

    @PostMapping("/project/{id}")
    public ResponseEntity<TaskDTO> createTask(@PathVariable("id") Long projectId, @Valid  @RequestBody TaskDTO task) {
        TaskDTO createdTask = taskService.createTask(projectId, task);
        return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
    }

    @GetMapping("/project/{id}")
    public ResponseEntity<List<TaskDTO>> getTasksByProjectIdAndStatus(@PathVariable("id") Long projectId, @RequestParam("status") String status) {
        List<TaskDTO> tasks = null;
        if (status != null) {
            tasks = taskService.getTasksByProjectIdAndStatus(projectId, status);
        } else {
            tasks = taskService.getTasksByProjectId(projectId);
        }
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    @GetMapping("/project/{id}/completed-after/{dueDate}")
    public ResponseEntity<List<TaskDTO>> getCompletedTasksAfterEstimatedTime(@PathVariable("id") Long projectId, @PathVariable("dueDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dueDate) {
        List<TaskDTO> tasks = taskService.getCompletedTasksAfterEstimatedTime(projectId, dueDate);
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }
}

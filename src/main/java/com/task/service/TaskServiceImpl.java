package com.task.service;

import com.task.constants.StatusConstants;
import com.task.dto.TaskDTO;
import com.task.entity.Project;
import com.task.entity.Task;
import com.task.exception.TaskNotFoundException;
import com.task.exception.TaskValidationException;
import com.task.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.task.constants.StatusConstants.COMPLETED;
import static com.task.util.ConversionUtils.convertToTask;
import static com.task.util.ConversionUtils.convertToTaskDTO;

/**
 * Service class for managing Task entities.
 */
@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskRepository taskRepository;

    /**
     * Create a new task for a specific project.
     * @param projectId ID of the project.
     * @param taskDTO Task data.
     * @return TaskDTO containing the created task.
     */
    @Override
    public TaskDTO createTask(Long projectId, TaskDTO taskDTO) {
        Task task = convertToTask(taskDTO);
        Project project = new Project();
        project.setId(projectId);
        task.setProject(project);
        return convertToTaskDTO(taskRepository.save(task));
    }

    /**
     * Update a task by its ID.
     * @param taskId The ID of the task to update.
     * @param updatedTask The updated task data.
     * @return TaskDTO containing the updated task.
     * @throws TaskValidationException if the task status is not "In Progress" or "Pending".
     * @throws TaskNotFoundException  if the task is not found.
     */
    @Override
    public TaskDTO updateTask(Long taskId, TaskDTO updatedTask) {
        Optional<Task> existingTaskOptional = taskRepository.findById(taskId);

        if (existingTaskOptional.isPresent()) {
            Task existingTask = existingTaskOptional.get();
            // Check if status is "In Progress" or "Pending" before update.
            if (StatusConstants.IN_PROGRESS.equals(existingTask.getStatus()) || StatusConstants.PENDING.equals(existingTask.getStatus())) {
                Task task = convertToTask(updatedTask);
                // Save the updated task
                return convertToTaskDTO(taskRepository.save(task));
            } else {
                throw new TaskValidationException("Cannot delete task with status: " + existingTask.getStatus());
            }
        } else {
            throw new TaskNotFoundException("Task not found with id: " + taskId);
        }
    }

    /**
     * Delete a task by its ID.
     * @param taskId The ID of the task to delete.
     * @throws TaskValidationException if the task status is not "In Progress" or "Pending".
     * @throws TaskNotFoundException  if the task is not found.
     */
    @Override
    public void deleteTask(Long taskId) {
        Optional<Task> taskOptional = taskRepository.findById(taskId);
        if (taskOptional.isPresent()) {
            Task task = taskOptional.get();
            // Check if status is "In Progress" or "Pending" before deletion
            if (StatusConstants.IN_PROGRESS.equals(task.getStatus()) || StatusConstants.PENDING.equals(task.getStatus())) {
                taskRepository.deleteById(taskId);
            } else {
                throw new TaskValidationException("Cannot delete task with status: " + task.getStatus());
            }
        } else {
            throw new TaskNotFoundException("Task not found with id: " + taskId);
        }
    }

    /**
     * Retrieve a task by its ID.
     * @param taskId The ID of the task to retrieve.
     * @return TaskDTO containing the retrieved task.
     * @throws TaskNotFoundException if the task is not found.
     */
    @Override
    public TaskDTO getTaskById(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found with id: " + taskId));

        return convertToTaskDTO(task);
    }

    /**
     * Retrieve a paginated list of tasks.
     * @param page Page number.
     * @param size Number of tasks per page.
     * @param sortBy Sorting criteria (e.g., "dueDate").
     * @return List of TaskDTOs.
     */
    @Override
    public List<TaskDTO> getAllTasks(int page, int size, String sortBy) {
        Page<Task> taskPage = taskRepository.findAll(PageRequest.of(page, size, Sort.by(sortBy)));

        return taskPage.getContent().stream()
                .map(task -> convertToTaskDTO(task))
                .collect(Collectors.toList());
    }

    /**
     * Update the status of multiple tasks in a batch.
     * @param taskIds List of task IDs to update.
     * @param status status for the tasks.
     */
    @Transactional
    @Override
    public void updateBatchTaskStatus(List<Long> taskIds, String status) {
        List<Task> tasks = taskRepository.findAllById(taskIds);
        tasks.forEach(task -> task.setStatus(status));
        taskRepository.saveAll(tasks);
    }

    @Override
    public List<TaskDTO> getOverdueTasks() {
        LocalDate currentDate = LocalDate.now();
        List<Task> overdueTasks = taskRepository.findByDueDateBeforeAndStatusNotEqualCompleted(currentDate);
        return overdueTasks.stream()
                .map(overDueTask -> convertToTaskDTO(overDueTask))
                .collect(Collectors.toList());
    }

    /**
     * Retrieve all tasks associated with a specific project.
     * @param projectId ID of the project.
     * @return List of TaskDTOs representing tasks associated with the project.
     */
    @Override
    public List<TaskDTO> getTasksByProjectId(Long projectId) {
        List<Task> projectTasks = taskRepository.findByProjectId(projectId);
        return projectTasks.stream()
                .map(overDueTask -> convertToTaskDTO(overDueTask))
                .collect(Collectors.toList());
    }

    /**
     * Retrieve tasks associated with a specific project and matching a given status.
     * @param projectId ID of the project.
     * @param status Status to filter tasks.
     * @return List of TaskDTOs representing tasks matching the project and status.
     */
    public List<TaskDTO> getTasksByProjectIdAndStatus(Long projectId, String status) {
        List<Task> projectTasks = taskRepository.findByProjectIdAndStatus(projectId, status);
        return projectTasks.stream()
                .map(overDueTask -> convertToTaskDTO(overDueTask))
                .collect(Collectors.toList());
    }

    /**
     * Retrieve tasks associated with a specific project that are completed after a certain due date.
     * @param projectId ID of the project.
     * @param dueDate Due date to filter completed tasks.
     * @return List of TaskDTOs representing completed tasks after the specified due date.
     */
    public List<TaskDTO> getCompletedTasksAfterEstimatedTime(Long projectId, LocalDate dueDate) {
        List<Task> projectTasks = taskRepository.findByProjectIdAndStatusAndDueDateAfter(projectId, COMPLETED, dueDate);
        return projectTasks.stream()
                .map(overDueTask -> convertToTaskDTO(overDueTask))
                .collect(Collectors.toList());
    }
}


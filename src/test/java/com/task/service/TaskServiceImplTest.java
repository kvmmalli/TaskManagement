package com.task.service;

import com.task.dto.TaskDTO;
import com.task.entity.Task;
import com.task.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TaskServiceImplTest {

    @InjectMocks
    private TaskServiceImpl taskService;

    @Mock
    private TaskRepository taskRepository;

    @Test
    public void testCreateTask() {
        // Mock data
        Long projectId = 1L;
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setTitle("Sample Task");
        taskDTO.setDescription("Task description");
        taskDTO.setStatus("In Progress");
        taskDTO.setPriority(1);

        when(taskRepository.save(any())).thenAnswer(invocation -> invocation.getArguments()[0]);

        TaskDTO result = taskService.createTask(projectId, taskDTO);

        Mockito.verify(taskRepository, times(1)).save(any());

        assertNotNull(result);
        assertEquals("Sample Task", result.getTitle());
    }

    @Test
    public void testUpdateTask() {
        // Mock data
        Long taskId = 1L;
        TaskDTO updatedTaskDTO = new TaskDTO();
        updatedTaskDTO.setTitle("Updated Task");
        updatedTaskDTO.setDescription("Updated description");
        updatedTaskDTO.setStatus("In Progress");
        updatedTaskDTO.setPriority(2);

        Task existingTask = new Task();
        existingTask.setId(taskId);
        existingTask.setTitle("Sample Task");
        existingTask.setDescription("Task description");
        existingTask.setStatus("In Progress");
        existingTask.setPriority(1);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(any())).thenAnswer(invocation -> invocation.getArguments()[0]);

        TaskDTO result = taskService.updateTask(taskId, updatedTaskDTO);

        Mockito.verify(taskRepository, times(1)).findById(taskId);
        Mockito.verify(taskRepository, times(1)).save(any());

        assertNotNull(result);
        assertEquals("Updated Task", result.getTitle());
    }
}


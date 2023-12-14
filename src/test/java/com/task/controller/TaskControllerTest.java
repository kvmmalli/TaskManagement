package com.task.controller;
import com.task.dto.TaskDTO;
import com.task.service.TaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import static com.task.constants.StatusConstants.IN_PROGRESS;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private TaskService taskService;

    @Test
    public void testCreateTask() throws Exception {

        LocalDate futureDate = LocalDate.now().plusDays(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = futureDate.format(formatter);
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setProjectName("Project1");
        taskDTO.setTitle("Sample Task");
        taskDTO.setDescription("Task description");
        taskDTO.setStatus(IN_PROGRESS);
        taskDTO.setPriority(1);
        taskDTO.setDueDate(futureDate);
        when(taskService.createTask(any(), any())).thenReturn(taskDTO);

        // Sending a POST request to create a task
        mockMvc.perform(post("/api/v1/projects/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\": \"Sample Task\", \"description\": \"Task description\", \"status\": \"In Progress\", \"priority\": 1, \"dueDate\": \"" + formattedDate + "\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Sample Task"))
                .andExpect(jsonPath("$.description").value("Task description"))
                .andExpect(jsonPath("$.priority").value(1))
                .andExpect(jsonPath("$.status").value(IN_PROGRESS))
                .andExpect(jsonPath("$.dueDate").value(formattedDate));

    }

    @Test
    public void testUpdateTask() throws Exception {
        Long taskId = 1L;
        LocalDate futureDate = LocalDate.now().plusDays(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = futureDate.format(formatter);
        TaskDTO updatedTaskDTO = new TaskDTO();
        updatedTaskDTO.setProjectName("Project1");
        updatedTaskDTO.setTitle("Update Task");
        updatedTaskDTO.setDescription("Task description");
        updatedTaskDTO.setStatus(IN_PROGRESS);
        updatedTaskDTO.setPriority(1);
        updatedTaskDTO.setDueDate(futureDate);

        when(taskService.updateTask(eq(taskId), any(TaskDTO.class))).thenReturn(updatedTaskDTO);

        mockMvc.perform(put("/api/v1/projects/tasks/{id}", taskId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\": \"Update Task\", \"description\": \"Task description\", \"status\": \"In Progress\", \"priority\": 1, \"dueDate\": \"" + formattedDate + "\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Update Task"));
    }

    @Test
    public void testDeleteTask() throws Exception {
        Long taskId = 1L;

        mockMvc.perform(delete("/api/v1/projects/tasks/{id}", taskId))
                .andExpect(status().isOk())
                .andExpect(content().string("Task deleted successfully"));
    }

    @Test
    public void testGetAllTasks() throws Exception {
        mockMvc.perform(get("/api/v1/projects"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    public void testGetTasksByProjectIdAndStatus() throws Exception {
        Long projectId = 1L;
        String status = IN_PROGRESS;

        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setTitle("Sample Task");
        when(taskService.getTasksByProjectIdAndStatus(eq(projectId), eq(status))).thenReturn(Arrays.asList(taskDTO));
        mockMvc.perform(get("/api/v1/projects/{id}?status={status}", projectId, status))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Sample Task"));
    }

    @Test
    public void testGetCompletedTasksAfterEstimatedTime() throws Exception {
        Long projectId = 1L;
        LocalDate dueDate = LocalDate.now();
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setTitle("Completed Task");
        taskDTO.setDueDate(dueDate.minusDays(2));
        when(taskService.getCompletedTasksAfterEstimatedTime(eq(projectId), eq(dueDate))).thenReturn(Arrays.asList(taskDTO));

        mockMvc.perform(get("/api/v1/projects/{id}/completed-tasks-after/{dueDate}", projectId, dueDate)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Completed Task"));
    }
}

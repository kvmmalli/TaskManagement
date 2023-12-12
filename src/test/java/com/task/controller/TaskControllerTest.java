package com.task.controller;
import com.task.constants.StatusConstants;
import com.task.dto.TaskDTO;
import com.task.service.TaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
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
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setTitle("Sample Task");
        taskDTO.setDescription("Task description");
        taskDTO.setStatus(StatusConstants.IN_PROGRESS);
        taskDTO.setPriority(1);
        taskDTO.setDueDate(LocalDate.now().plusDays(1));  // Set a future date
        when(taskService.createTask(any(), any())).thenReturn(taskDTO);

        // Sending a POST request to create a task
        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\": \"Sample Task\", \"description\": \"Task description\", \"status\": \"In Progress\", \"priority\": 1, \"dueDate\": \"2023-12-13\"}")) // Adjust the content accordingly
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Sample Task"))
                .andExpect(jsonPath("$.description").value("Task description"))
                .andExpect(jsonPath("$.priority").value(1))
                .andExpect(jsonPath("$.status").value(StatusConstants.IN_PROGRESS))
                .andExpect(jsonPath("$.dueDate").value("2023-12-13"));

    }

    @Test
    public void testUpdateTask() throws Exception {
        // Mocking the service response
        TaskDTO updatedTaskDTO = new TaskDTO();
        when(taskService.updateTask(eq(1L), any(TaskDTO.class))).thenReturn(updatedTaskDTO);

        // Sending a PUT request to update a task
        mockMvc.perform(put("/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Updated Task Name\"}"))  // Adjust the content accordingly
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Task Name"));  // Adjust the expected values accordingly
    }

    @Test
    public void testDeleteTask() throws Exception {
        // No need to mock the service response for a delete operation

        // Sending a DELETE request to delete a task
        mockMvc.perform(delete("/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Task deleted successfully"));
    }

    @Test
    public void testGetTaskById() throws Exception {
        // Mocking the service response
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setTaskId(1l);
        taskDTO.setTitle("Some Title");
        when(taskService.getTaskById(eq(1L))).thenReturn(taskDTO);

        // Sending a GET request to get a task by ID
        mockMvc.perform(get("/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists());  // Adjust the expected values accordingly
    }

    @Test
    public void testGetAllTasks() throws Exception {
        // Mocking the service response
        List<TaskDTO> taskDTOList = Arrays.asList(new TaskDTO(), new TaskDTO());
        when(taskService.getAllTasks(anyInt(), anyInt(), anyString())).thenReturn(taskDTOList);

        // Sending a GET request to get all tasks
        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));  // Adjust the expected values accordingly
    }
}

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

        LocalDate futureDate = LocalDate.now().plusDays(1);
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setTitle("Sample Task");
        taskDTO.setDescription("Task description");
        taskDTO.setStatus(StatusConstants.IN_PROGRESS);
        taskDTO.setPriority(1);
        taskDTO.setDueDate(futureDate);
        when(taskService.createTask(any(), any())).thenReturn(taskDTO);

        // Sending a POST request to create a task
        mockMvc.perform(post("/api/v1/tasks/project/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\": \"Sample Task\", \"description\": \"Task description\", \"status\": \"In Progress\", \"priority\": 1, \"dueDate\": \" + futureDate + \"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Sample Task"))
                .andExpect(jsonPath("$.description").value("Task description"))
                .andExpect(jsonPath("$.priority").value(1))
                .andExpect(jsonPath("$.status").value(StatusConstants.IN_PROGRESS))
                .andExpect(jsonPath("$.dueDate").value(futureDate));

    }

    @Test
    public void testUpdateTask() throws Exception {
        // Mocking the service response
        TaskDTO updatedTaskDTO = new TaskDTO();
        when(taskService.updateTask(eq(1L), any(TaskDTO.class))).thenReturn(updatedTaskDTO);

        // Sending a PUT request to update a task
        mockMvc.perform(put("/api/v1/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Updated Task Name\"}"))  // Adjust the content accordingly
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Task Name"));  // Adjust the expected values accordingly
    }

    @Test
    public void testDeleteTask() throws Exception {
        mockMvc.perform(delete("/api/v1/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Task deleted successfully"));
    }

    @Test
    public void testGetTaskById() throws Exception {
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setTaskId(1l);
        taskDTO.setTitle("Some Title");
        when(taskService.getTaskById(eq(1L))).thenReturn(taskDTO);

        mockMvc.perform(get("/api/v1/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.taskId").exists());
    }

    @Test
    public void testGetAllTasks() throws Exception {
        List<TaskDTO> taskDTOList = Arrays.asList(new TaskDTO(), new TaskDTO());
        when(taskService.getAllTasks(anyInt(), anyInt(), anyString())).thenReturn(taskDTOList);

        mockMvc.perform(get("/api/v1/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }
}

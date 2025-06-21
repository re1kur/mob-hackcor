package re1kur.uas.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import re1kur.core.dto.UserTaskDto;
import re1kur.core.exception.UserTaskNotFoundException;
import re1kur.uas.controller.task.UserTaskController;
import re1kur.uas.enums.Status;
import re1kur.uas.service.UserTaskService;

import java.util.List;
import java.util.UUID;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(UserTaskController.class)
@AutoConfigureMockMvc(addFilters = false)
public class UserTaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserTaskService service;

    private final UserTaskDto dummyTask = new UserTaskDto(
            UUID.randomUUID().toString(),
            1L,
            1L,
            Status.pending.name());

    @Test
    void getAllTasks_shouldReturnList() throws Exception {
        given(service.getAllByUser(dummyTask.userId())).willReturn(List.of(dummyTask));

        mockMvc.perform(get("/api/user-task")
                        .param("userId", dummyTask.userId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].taskId").value(1L));
    }

    @Test
    void getTaskById_shouldReturnTask() throws Exception {
        given(service.getById(dummyTask.userId(), 1L)).willReturn(dummyTask);

        mockMvc.perform(get("/api/user-task/1")
                        .param("userId", dummyTask.userId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.taskId").value(1L));
    }

    @Test
    void getTaskById_shouldReturn404_whenNotFound() throws Exception {
        given(service.getById(dummyTask.userId(), 99L)).willThrow(new UserTaskNotFoundException("Task not found"));

        mockMvc.perform(get("/api/user-task/99")
                        .param("userId", dummyTask.userId()))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Task not found"));
    }

    @Test
    void updateStatus_shouldUpdateAndReturnTask() throws Exception {
        mockMvc.perform(put("/api/user-task/1/update-status")
                        .param("userId", dummyTask.userId())
                        .param("status", "confirmed"))
                .andExpect(status().isOk());
    }

    @Test
    void updateStatus_shouldReturn404_whenNotFound() throws Exception {
        doThrow(new UserTaskNotFoundException("Task not found"))
                .when(service).updateStatus(dummyTask.userId(), 999L, Status.confirmed.name());

        mockMvc.perform(put("/api/user-task/999/update-status")
                        .param("userId", dummyTask.userId())
                        .param("status", "confirmed"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Task not found"));
    }

    @Test
    void deleteTask_shouldSucceed() throws Exception {
        doNothing().when(service).delete(dummyTask.userId(), 1L);

        mockMvc.perform(delete("/api/user-task/1/delete")
                        .param("userId", dummyTask.userId()))
                .andExpect(status().isOk());
    }

    @Test
    void deleteTask_shouldReturn404_whenNotFound() throws Exception {
        doThrow(new UserTaskNotFoundException("Task not found")).when(service).delete(dummyTask.userId(), 1L);

        mockMvc.perform(delete("/api/user-task/1/delete")
                        .param("userId", dummyTask.userId()))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Task not found"));
    }
}


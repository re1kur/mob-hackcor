package re1kur.uas.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import re1kur.core.dto.TaskAttemptDto;
import re1kur.core.exception.TaskAttemptNotFoundException;
import re1kur.core.payload.TaskAttemptPayload;
import re1kur.core.payload.TaskAttemptUpdatePayload;
import re1kur.uas.controller.task.TaskAttemptController;
import re1kur.uas.service.TaskAttemptService;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = TaskAttemptController.class)
@AutoConfigureMockMvc(addFilters = false)
public class TaskAttemptControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockitoBean
    private TaskAttemptService service;

    private static final String URL = "/api/task-attempt";
    private UUID userId;
    private UUID moderatorId;
    private UUID fileId;
    private LocalDateTime now;

    @BeforeEach
    public void setup() {
        userId = UUID.randomUUID();
        moderatorId = UUID.randomUUID();
        fileId = UUID.randomUUID();
        now = LocalDateTime.now();
    }

    @Test
    void testCreate_ValidPayload_ReturnsCreated() throws Exception {
        TaskAttemptPayload payload = TaskAttemptPayload.builder()
                .userId(userId.toString())
                .taskId(1L)
                .fileContentId(fileId.toString())
                .textContent("some text")
                .build();

        TaskAttemptDto response = TaskAttemptDto.builder()
                .id(1L)
                .userId(userId.toString())
                .taskId(1L)
                .fileContentId(fileId.toString())
                .textContent("some text")
                .attemptTime(now)
                .build();

        Mockito.when(service.create(payload)).thenReturn(response);

        mvc.perform(MockMvcRequestBuilders.post(URL + "/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(payload)))
                .andExpect(status().isCreated())
                .andExpect(content().json(mapper.writeValueAsString(response)));

        Mockito.verify(service, times(1)).create(payload);
    }


    @Test
    void testGetById_ExistingId_ReturnsDto() throws Exception {
        TaskAttemptDto dto = TaskAttemptDto.builder()
                .id(1L)
                .userId(userId.toString())
                .taskId(1L)
                .fileContentId(fileId.toString())
                .textContent("get attempt")
                .attemptTime(now)
                .build();

        Mockito.when(service.getById(1L)).thenReturn(dto);

        mvc.perform(MockMvcRequestBuilders.get(URL + "/get")
                        .param("id", "1"))
                .andExpect(status().isFound())
                .andExpect(content().json(mapper.writeValueAsString(dto)));

        Mockito.verify(service, times(1)).getById(1L);
    }

    @Test
    void testUpdate_ValidPayload_ReturnsUpdatedDto() throws Exception {
        TaskAttemptUpdatePayload updatePayload = TaskAttemptUpdatePayload.builder()
                .id(1L)
                .userId(userId.toString())
                .dailyTaskId(1L)
                .fileContentId(fileId.toString())
                .textContent("updated")
                .moderatorId(moderatorId.toString())
                .confirmed(true)
                .attemptTime(now)
                .build();

        TaskAttemptDto updated = TaskAttemptDto.builder()
                .id(1L)
                .userId(userId.toString())
                .taskId(1L)
                .fileContentId(fileId.toString())
                .textContent("updated")
                .moderatorId(moderatorId.toString())
                .confirmed(true)
                .attemptTime(now)
                .build();

        Mockito.when(service.update(updatePayload)).thenReturn(updated);

        mvc.perform(MockMvcRequestBuilders.put(URL + "/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(updatePayload)))
                .andExpect(status().isCreated())
                .andExpect(content().json(mapper.writeValueAsString(updated)));

        Mockito.verify(service, times(1)).update(updatePayload);
    }

    @Test
    void testUpdate__NotExistingAttempt__ReturnsNotFound() throws Exception {
        TaskAttemptUpdatePayload updatePayload = TaskAttemptUpdatePayload.builder()
                .id(1L)
                .userId(userId.toString())
                .dailyTaskId(1L)
                .fileContentId(fileId.toString())
                .textContent("updated")
                .moderatorId(moderatorId.toString())
                .confirmed(true)
                .attemptTime(now)
                .build();

        Mockito.when(service.update(updatePayload)).thenThrow(TaskAttemptNotFoundException.class);

        mvc.perform(MockMvcRequestBuilders.put(URL + "/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(updatePayload)))
                .andExpect(status().isNotFound());

        Mockito.verify(service, times(1)).update(updatePayload);
    }

    @Test
    void testDelete__ValidId__ReturnsOk() throws Exception {
        Long id = 1L;

        mvc.perform(MockMvcRequestBuilders
                        .delete(URL + "/delete")
                        .param("id", id.toString()))
                .andExpect(status().isOk());

        Mockito.verify(service, times(1)).delete(id);
    }

    @Test
    void testDelete__InvalidId__ReturnsNotFound() throws Exception {
        Long id = 1L;

        Mockito.doThrow(TaskAttemptNotFoundException.class).when(service).delete(1L);

        mvc.perform(MockMvcRequestBuilders
                        .delete(URL + "/delete")
                        .param("id", id.toString()))
                .andExpect(status().isNotFound());

        Mockito.verify(service, times(1)).delete(id);
    }
}

package re1kur.uas.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.MultiValueMap;
import re1kur.core.dto.DailyTaskDto;
import re1kur.core.exception.TaskAlreadyExistException;
import re1kur.core.exception.TaskNotFoundException;
import re1kur.core.payload.DailyTaskPayload;
import re1kur.core.payload.DailyTaskUpdatePayload;
import re1kur.uas.service.DailyTaskService;

import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = DailyTaskController.class)
@AutoConfigureMockMvc(addFilters = false)
public class DailyTaskControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockitoBean
    private DailyTaskService service;

    private static final String URL = "/api/daily-task";

    @Test
    void testCreate__ValidTask__DoesNotThrowException() throws Exception {
        DailyTaskPayload payload = DailyTaskPayload.builder()
                .title("title")
                .description("description")
                .reward(5)
                .build();

        DailyTaskDto expected = DailyTaskDto.builder()
                .id(1L)
                .title("title")
                .description("description")
                .reward(5)
                .build();

        Mockito.when(service.create(payload)).thenReturn(DailyTaskDto.builder()
                .id(1L)
                .title("title")
                .description("description")
                .reward(5)
                .build());

        mvc.perform(MockMvcRequestBuilders
                        .post(URL + "/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(payload)))
                .andExpect(status().isCreated())
                .andExpect(content().json(mapper.writeValueAsString(expected)));

        Mockito.verify(service, Mockito.times(1)).create(payload);
    }

    @Test
    void testCreate__TaskWithInvalidTitle__ThrowsMethodArgumentNotValidException() throws Exception {
        DailyTaskPayload payload = DailyTaskPayload.builder()
                .title("")
                .description("description")
                .reward(5)
                .build();

        mvc.perform(MockMvcRequestBuilders
                        .post(URL + "/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(payload)))
                .andExpect(status().isBadRequest());

        Mockito.verifyNoInteractions(service);
    }

    @Test
    void testCreate__TaskWithInvalidDescription__ThrowsMethodArgumentNotValidException() throws Exception {
        DailyTaskPayload payload = DailyTaskPayload.builder()
                .title("title")
                .description("")
                .reward(5)
                .build();

        mvc.perform(MockMvcRequestBuilders
                        .post(URL + "/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(payload)))
                .andExpect(status().isBadRequest());

        Mockito.verifyNoInteractions(service);
    }

    @Test
    void testCreate__TaskAlreadyExists__ThrowsTaskAlreadyExistException() throws Exception {
        DailyTaskPayload payload = DailyTaskPayload.builder()
                .title("title")
                .description("description")
                .reward(5)
                .build();

        Mockito.when(service.create(payload)).thenThrow(TaskAlreadyExistException.class);

        mvc.perform(MockMvcRequestBuilders
                        .post(URL + "/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(payload)))
                .andExpect(status().isBadRequest());

        Mockito.verify(service, Mockito.times(1)).create(payload);
    }

    @Test
    void testGet__ValidTask__DoesNotThrowException() throws Exception {
        long id = 1L;
        DailyTaskDto expected = DailyTaskDto.builder()
                .id(1L)
                .title("title")
                .description("description")
                .reward(5)
                .build();

        Mockito.when(service.getById(1L)).thenReturn(DailyTaskDto.builder()
                .id(1L)
                .title("title")
                .description("description")
                .reward(5)
                .build());

        mvc.perform(MockMvcRequestBuilders
                        .get(URL + "/get")
                        .param("id", Long.toString(id)))
                .andExpect(status().isFound())
                .andExpect(content().json(mapper.writeValueAsString(expected)));

        Mockito.verify(service, Mockito.times(1)).getById(id);
    }

    @Test
    void testGet__NotExistingTask__ThrowTaskNotFoundException() throws Exception {
        long id = 1L;

        Mockito.when(service.getById(1L)).thenThrow(TaskNotFoundException.class);

        mvc.perform(MockMvcRequestBuilders
                        .get(URL + "/get")
                        .param("id", Long.toString(id)))
                .andExpect(status().isNotFound());

        Mockito.verify(service, Mockito.times(1)).getById(id);
    }

    @Test
    void testUpdate__ValidTask__DoesNotThrowException() throws Exception {
        DailyTaskUpdatePayload payload = DailyTaskUpdatePayload.builder()
                .id(1L)
                .title("titleUpdate")
                .description("descriptionUpdate")
                .reward(10)
                .build();
        DailyTaskDto expected = DailyTaskDto.builder()
                .id(1L)
                .title("titleUpdate")
                .description("descriptionUpdate")
                .reward(10)
                .build();

        Mockito.when(service.update(payload)).thenReturn(expected);

        mvc.perform(MockMvcRequestBuilders
                        .put(URL + "/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(payload)))
                .andExpect(status().isCreated())
                .andExpect(content().json(mapper.writeValueAsString(expected)));

        Mockito.verify(service, Mockito.times(1)).update(payload);
    }

    @Test
    void testUpdate__NotExistingTask__ThrowTaskNotFoundException() throws Exception {
        DailyTaskUpdatePayload payload = DailyTaskUpdatePayload.builder()
                .id(1L)
                .title("titleUpdate")
                .description("descriptionUpdate")
                .reward(10)
                .build();

        Mockito.when(service.update(payload)).thenThrow(TaskNotFoundException.class);

        mvc.perform(MockMvcRequestBuilders
                        .put(URL + "/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(payload)))
                .andExpect(status().isNotFound());

        Mockito.verify(service, Mockito.times(1)).update(payload);
    }

    @Test
    void testUpdate__TaskWithThisTitleAlreadyExist__TaskTaskAlreadyExistException() throws Exception {
        DailyTaskUpdatePayload payload = DailyTaskUpdatePayload.builder()
                .id(1L)
                .title("titleUpdate")
                .description("descriptionUpdate")
                .reward(10)
                .build();

        Mockito.when(service.update(payload)).thenThrow(TaskAlreadyExistException.class);

        mvc.perform(MockMvcRequestBuilders
                        .put(URL + "/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(payload)))
                .andExpect(status().isBadRequest());

        Mockito.verify(service, Mockito.times(1)).update(payload);
    }

    @Test
    void testDelete__ValidTask__DoesNotThrowException() throws Exception {
        long id = 1L;

        Mockito.doNothing().when(service).delete(1L);

        mvc.perform(MockMvcRequestBuilders
                        .delete(URL + "/delete")
                        .param("id", Long.toString(id)))
                .andExpect(status().isOk());

        Mockito.verify(service, Mockito.times(1)).delete(1L);
    }

    @Test
    void testDelete__NotExistingTask__ThrowTaskNotFoundException() throws Exception {
        long id = 1L;

        Mockito.doThrow(TaskNotFoundException.class).when(service).delete(1L);

        mvc.perform(MockMvcRequestBuilders
                        .delete(URL + "/delete")
                        .param("id", Long.toString(id)))
                .andExpect(status().isNotFound());

        Mockito.verify(service, Mockito.times(1)).delete(1L);
    }

    @Test
    void getList__DoesNotThrowException() throws Exception {
        int pageNumber = 1;
        int pageSize = 10;
        DailyTaskDto build1 = DailyTaskDto.builder()
                .id(1L)
                .title("title")
                .description("description")
                .reward(5)
                .build();
        DailyTaskDto build2 = DailyTaskDto.builder()
                .id(2L)
                .title("title2")
                .description("description2")
                .reward(52)
                .build();
        List<DailyTaskDto> expected = List.of(build1, build2);
        Pageable pageable = PageRequest.of(1, 10);

        Mockito.when(service.getDailyTasks(pageable)).thenReturn(List.of(build1, build2));

        mvc.perform(MockMvcRequestBuilders
                        .get(URL + "/getPage")
                        .params(MultiValueMap.fromSingleValue(
                                Map.of("page", String.valueOf(pageNumber), "size", String.valueOf(pageSize)))))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(expected)));
    }
}

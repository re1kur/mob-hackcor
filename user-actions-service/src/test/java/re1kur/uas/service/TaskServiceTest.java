package re1kur.uas.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import re1kur.core.dto.TaskDto;
import re1kur.core.exception.TaskAlreadyExistException;
import re1kur.core.exception.TaskNotFoundException;
import re1kur.core.payload.TaskPayload;
import re1kur.core.payload.TaskUpdatePayload;
import re1kur.uas.entity.Task;
import re1kur.uas.mapper.impl.TaskMapperImpl;
import re1kur.uas.repository.TaskRepository;
import re1kur.uas.service.impl.TaskServiceImpl;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {
    @InjectMocks
    private TaskServiceImpl service;

    @Mock
    private TaskRepository repo;

    @Mock
    private TaskMapperImpl mapper;

    @Test
    void testCreate__ValidTask__DoesNotThrowException() {
        TaskPayload payload = TaskPayload.builder()
                .title("title")
                .description("description")
                .reward(5)
                .build();
        Task mapped = Task.builder()
                .title("title")
                .description("description")
                .reward(5)
                .build();
        Task saved = Task.builder()
                .id(1L)
                .title("title")
                .description("description")
                .reward(5)
                .build();

        TaskDto expected = TaskDto.builder()
                .id(1L)
                .title("title")
                .description("description")
                .reward(5)
                .build();

        Mockito.when(repo.existsByTitle("title")).thenReturn(false);
        Mockito.when(mapper.write(payload)).thenReturn(mapped);
        Mockito.when(repo.save(mapped)).thenReturn(saved);
        Mockito.when(mapper.read(saved)).thenReturn(expected);

        TaskDto result = Assertions.assertDoesNotThrow(() -> service.create(payload));
        Assertions.assertEquals(expected, result);

        Mockito.verify(repo, Mockito.times(1)).existsByTitle("title");
        Mockito.verify(repo, Mockito.times(1)).save(mapped);
        Mockito.verify(mapper, Mockito.times(1)).write(payload);
        Mockito.verify(mapper, Mockito.times(1)).read(saved);
    }

    @Test
    void testCreate__TaskAlreadyExist__ThrowTaskAlreadyExistException() {
        TaskPayload payload = TaskPayload.builder()
                .title("title")
                .description("description")
                .reward(5)
                .build();

        Mockito.when(repo.existsByTitle("title")).thenReturn(true);

        Assertions.assertThrows(TaskAlreadyExistException.class, () -> service.create(payload));

        Mockito.verify(repo, Mockito.times(1)).existsByTitle("title");
        Mockito.verifyNoMoreInteractions(repo);
        Mockito.verifyNoInteractions(mapper);
    }

    @Test
    void testGet__ValidTask__DoesNotThrowException() {
        long id = 1L;
        Task found = Task.builder()
                .id(1L)
                .title("title")
                .description("description")
                .reward(5)
                .build();
        TaskDto expected = TaskDto.builder()
                .id(1L)
                .title("title")
                .description("description")
                .reward(5)
                .build();

        Mockito.when(repo.findById(1L)).thenReturn(Optional.of(found));
        Mockito.when(mapper.read(found)).thenReturn(TaskDto.builder()
                .id(1L)
                .title("title")
                .description("description")
                .reward(5)
                .build());

        TaskDto result = Assertions.assertDoesNotThrow(() -> service.getById(id));
        Assertions.assertEquals(expected, result);

        Mockito.verify(repo, Mockito.times(1)).findById(1L);
        Mockito.verify(mapper, Mockito.times(1)).read(found);
    }

    @Test
    void testGet__NotExistingTask__ThrowTaskNotFoundException() {
        long id = 1L;

        Mockito.when(repo.findById(1L)).thenReturn(Optional.empty());

        Assertions.assertThrows(TaskNotFoundException.class, () -> service.getById(id));

        Mockito.verify(repo, Mockito.times(1)).findById(1L);
        Mockito.verifyNoMoreInteractions(repo);
        Mockito.verifyNoInteractions(mapper);
    }

    @Test
    void testUpdate__ValidTask__DoesNotThrowException() {
        TaskUpdatePayload payload = TaskUpdatePayload.builder()
                .id(1L)
                .title("titleUpdate")
                .description("descriptionUpdate")
                .reward(10)
                .build();
        Task found = Task.builder()
                .id(1L)
                .title("title")
                .description("description")
                .reward(5)
                .build();
        Task updated = Task.builder()
                .id(1L)
                .title("titleUpdate")
                .description("descriptionUpdate")
                .reward(10)
                .build();
        TaskDto expected = TaskDto.builder()
                .id(1L)
                .title("titleUpdate")
                .description("descriptionUpdate")
                .reward(10)
                .build();

        Mockito.when(repo.findById(1L)).thenReturn(Optional.of(found));
        Mockito.when(mapper.update(found, payload)).thenReturn(updated);
        Mockito.when(repo.save(updated)).thenReturn(updated);
        Mockito.when(mapper.read(updated)).thenReturn(TaskDto.builder()
                .id(1L)
                .title("titleUpdate")
                .description("descriptionUpdate")
                .reward(10)
                .build());

        TaskDto result = Assertions.assertDoesNotThrow(() -> service.update(payload));
        Assertions.assertEquals(expected, result);

        Mockito.verify(repo, Mockito.times(1)).findById(1L);
        Mockito.verify(repo, Mockito.times(1)).save(updated);
        Mockito.verify(mapper, Mockito.times(1)).read(updated);
    }

    @Test
    void testUpdate__TaskWithThisTitleAlreadyExist__ThrowTaskAlreadyExistException() {
        TaskUpdatePayload payload = TaskUpdatePayload.builder()
                .id(1L)
                .title("titleUpdate")
                .description("descriptionUpdate")
                .reward(10)
                .build();
        Task found = Task.builder()
                .id(1L)
                .title("title")
                .description("description")
                .reward(5)
                .build();
        Task updated = Task.builder()
                .id(1L)
                .title("titleUpdate")
                .description("descriptionUpdate")
                .reward(10)
                .build();
        Mockito.when(repo.findById(1L)).thenReturn(Optional.of(found));
        Mockito.when(mapper.update(found, payload)).thenReturn(updated);
        Mockito.when(repo.save(Mockito.any(Task.class))).thenThrow(TaskAlreadyExistException.class);

        Assertions.assertThrows(TaskAlreadyExistException.class, () -> service.update(payload));

        Mockito.verify(repo, Mockito.times(1)).findById(1L);
        Mockito.verify(repo, Mockito.times(1)).save(Mockito.any(Task.class));
        Mockito.verify(mapper, Mockito.times(1)).update(found, payload);
        Mockito.verifyNoMoreInteractions(mapper);
    }

    @Test
    void testUpdate__NotExistingTask__ThrowTaskNotFoundException() {
        TaskUpdatePayload payload = TaskUpdatePayload.builder()
                .id(1L)
                .title("titleUpdate")
                .description("descriptionUpdate")
                .reward(10)
                .build();
        Mockito.when(repo.findById(1L)).thenThrow(TaskNotFoundException.class);

        Assertions.assertThrows(TaskNotFoundException.class, () -> service.update(payload));

        Mockito.verify(repo, Mockito.times(1)).findById(1L);
        Mockito.verifyNoMoreInteractions(repo);
        Mockito.verifyNoInteractions(mapper);
    }

    @Test
    void testDelete__ValidTask__DoesNotThrowException() {
        long id = 1L;

        Mockito.when(repo.existsById(id)).thenReturn(true);
        Mockito.doNothing().when(repo).deleteById(1L);

        Assertions.assertDoesNotThrow(() -> service.delete(id));

        Mockito.verify(repo, Mockito.times(1)).deleteById(id);
    }

    @Test
    void testDelete__NotExistingTask__ThrowTaskNotFoundException() {
        long id = 1L;

        Mockito.when(repo.existsById(id)).thenReturn(false);

        Assertions.assertThrows(TaskNotFoundException.class, () -> service.delete(id));

        Mockito.verify(repo, Mockito.times(1)).existsById(id);
        Mockito.verifyNoMoreInteractions(repo);
    }
}
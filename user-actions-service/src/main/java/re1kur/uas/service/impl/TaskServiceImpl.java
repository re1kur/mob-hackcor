package re1kur.uas.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import re1kur.core.dto.TaskDto;
import re1kur.core.exception.TaskAlreadyExistException;
import re1kur.core.exception.TaskNotFoundException;
import re1kur.core.payload.TaskPayload;
import re1kur.core.payload.TaskUpdatePayload;
import re1kur.uas.entity.Task;
import re1kur.uas.mapper.TaskMapper;
import re1kur.uas.repository.TaskRepository;
import re1kur.uas.service.TaskService;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskRepository repo;
    private final TaskMapper mapper;

    @Override
    @Transactional
    public TaskDto create(TaskPayload payload) {
        log.info("Received task payload for create: {}", payload.toString());
        if (repo.existsByTitle(payload.title()))
            throw new TaskAlreadyExistException("Task '%s' already exist.".formatted(payload.title()));
        Task mapped = mapper.write(payload);
        log.info("Task to save: {}", mapped.toString());
        Task saved = repo.save(mapped);
        if (payload.imageFileId() != null
            && payload.imageFileUrl() != null
            && payload.urlExpiresAt() != null) {
            saved.setTaskImage(mapper.taskImage(payload, saved));
            repo.save(saved);
        }
        return mapper.read(saved);
    }

    @Override
    public TaskDto getById(Long id) {
        Task task = repo.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task with id %s not found.".formatted(id)));
        return mapper.read(task);
    }

    @Override
    public TaskDto update(TaskUpdatePayload payload) {
        Long id = payload.id();
        Task task = repo.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task with id %s not found.".formatted(id)));
        Task updated = mapper.update(task, payload);
        Task saved = repo.save(updated);
        return mapper.read(saved);
    }

    @Override
    public void delete(Long id) {
        if (!repo.existsById(id)) throw new TaskNotFoundException("Task with id %s not found.".formatted(id));
        repo.deleteById(id);
    }

    @Override
    public List<TaskDto> getDailyTasks(Pageable pageable) {
        return repo.findAll(pageable).map(mapper::read).toList();
    }
}

package re1kur.uas.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import re1kur.core.dto.DailyTaskDto;
import re1kur.core.exception.TaskAlreadyExistException;
import re1kur.core.exception.TaskNotFoundException;
import re1kur.core.payload.DailyTaskPayload;
import re1kur.core.payload.DailyTaskUpdatePayload;
import re1kur.uas.entity.DailyTask;
import re1kur.uas.mapper.DailyTaskMapper;
import re1kur.uas.repository.DailyTaskRepository;
import re1kur.uas.service.DailyTaskService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DailyTaskServiceImpl implements DailyTaskService {
    private final DailyTaskRepository repo;
    private final DailyTaskMapper mapper;

    @Override
    public DailyTaskDto create(DailyTaskPayload payload) {
        if (repo.existsByTitle(payload.title()))
            throw new TaskAlreadyExistException("Task %s already exist.".formatted(payload.title()));
        DailyTask mapped = mapper.write(payload);
        DailyTask saved = repo.save(mapped);
        return mapper.read(saved);
    }

    @Override
    public DailyTaskDto getById(Long id) {
        DailyTask task = repo.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task with id %s not found.".formatted(id)));
        return mapper.read(task);
    }

    @Override
    public DailyTaskDto update(DailyTaskUpdatePayload payload) {
        Long id = payload.id();
        DailyTask task = repo.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task with id %s not found.".formatted(id)));
        DailyTask updated = mapper.update(task);
        DailyTask saved = repo.save(updated);
        return mapper.read(saved);
    }

    @Override
    public void delete(Long id) {
        if (!repo.existsById(id)) throw new TaskNotFoundException("Task with id %s not found.".formatted(id));
        repo.deleteById(id);
    }

    @Override
    public List<DailyTaskDto> getDailyTasks(Pageable pageable) {
        return repo.findAll(pageable).map(mapper::read).toList();
    }
}

package re1kur.uas.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import re1kur.core.dto.TaskAttemptDto;
import re1kur.core.exception.TaskAttemptNotFoundException;
import re1kur.core.exception.TaskNotFoundException;
import re1kur.core.exception.UserTaskNotFoundException;
import re1kur.core.payload.TaskAttemptPayload;
import re1kur.core.payload.TaskAttemptUpdatePayload;
import re1kur.uas.entity.Task;
import re1kur.uas.entity.TaskAttempt;
import re1kur.uas.entity.UserTaskId;
import re1kur.uas.entity.UserTask;
import re1kur.uas.mapper.TaskAttemptMapper;
import re1kur.uas.mapper.UserTaskMapper;
import re1kur.uas.repository.TaskRepository;
import re1kur.uas.repository.TaskAttemptRepository;
import re1kur.uas.repository.UserTaskRepository;
import re1kur.uas.service.TaskAttemptService;

@Service
@RequiredArgsConstructor
public class TaskAttemptServiceImpl implements TaskAttemptService {
    private final TaskAttemptRepository repo;
    private final TaskRepository dailyRepo;
    private final UserTaskRepository userTaskRepo;
    private final TaskAttemptMapper mapper;
    private final UserTaskMapper userTaskMapper;

    @Override
    public TaskAttemptDto create(TaskAttemptPayload payload) {
        Long id = payload.dailyTaskId();
        Task task = dailyRepo.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("DailyTask %s not found.".formatted(id)));

        TaskAttempt attempt = mapper.write(payload);
        attempt.setTask(task);

        TaskAttempt saved = repo.save(attempt);

        UserTask userTask = userTaskMapper.write(saved);

        userTaskRepo.save(userTask);
        return mapper.read(saved);
    }

    @Override
    public TaskAttemptDto getById(Long id) {
        TaskAttempt found = repo.findById(id)
                .orElseThrow(() -> new TaskAttemptNotFoundException("TaskAttempt %s not found".formatted(id)));
        return mapper.read(found);
    }

    @Override
    public TaskAttemptDto update(TaskAttemptUpdatePayload payload) {
        Long id = payload.id();
        TaskAttempt attempt = repo.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("TaskAttempt %s not found".formatted(id)));


        attempt = mapper.update(attempt, payload);

        TaskAttempt updated = repo.save(attempt);

        UserTask userTask = userTaskMapper.write(updated);

        userTaskRepo.save(userTask);

        return mapper.read(updated);
    }

    @Override
    public void delete(Long id) {
        TaskAttempt attempt = repo.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("TaskAttempt not found"));

        UserTaskId userTaskId = UserTaskId.builder()
                .userId(attempt.getUserId())
                .taskId(attempt.getTask().getId())
                .build();
        UserTask userTask = userTaskRepo.findById(userTaskId)
                .orElseThrow(() -> new UserTaskNotFoundException(
                        "Task %s was not found for the %s user.".formatted(userTaskId.getUserId().toString(), userTaskId.getTaskId())));
        userTask.setLastAttempt(null);
        userTaskRepo.save(userTask);

        repo.delete(attempt);
    }
}

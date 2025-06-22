package re1kur.uas.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
import re1kur.uas.enums.Status;
import re1kur.uas.mapper.TaskAttemptMapper;
import re1kur.uas.mapper.UserTaskMapper;
import re1kur.uas.repository.TaskRepository;
import re1kur.uas.repository.TaskAttemptRepository;
import re1kur.uas.repository.UserTaskRepository;
import re1kur.uas.service.EventService;
import re1kur.uas.service.TaskAttemptService;

@Service
@RequiredArgsConstructor
public class TaskAttemptServiceImpl implements TaskAttemptService {
    private final TaskAttemptRepository repo;
    private final TaskRepository taskRepo;
    private final UserTaskRepository userTaskRepo;
    private final TaskAttemptMapper mapper;
    private final UserTaskMapper userTaskMapper;
    private final EventService service;

    @Override
    @Transactional
    public TaskAttemptDto create(TaskAttemptPayload payload) {
        Long id = payload.taskId();
        Task task = taskRepo.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task %s not found.".formatted(id)));

        TaskAttempt attempt = mapper.write(payload);
        attempt.setTask(task);

        return getTaskAttemptDto(attempt);
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

        return getTaskAttemptDto(attempt);
    }

    private TaskAttemptDto getTaskAttemptDto(TaskAttempt attempt) {
        TaskAttempt updated = repo.save(attempt);

        UserTaskId userTaskId = UserTaskId.builder()
                .userId(updated.getUserId())
                .taskId(updated.getTask().getId())
                .build();

        UserTask userTask = userTaskRepo.findById(userTaskId).orElse(null);

        if (userTask == null) {
            userTask = userTaskMapper.write(updated);
        } else {
            userTask.setLastAttempt(updated);
            Boolean result = updated.getConfirmed();
            String status = result == null
                    ? Status.pending.name()
                    : result ? Status.confirmed.name() : Status.rejected.name();
            userTask.setStatus(status);
        }

        userTaskRepo.save(userTask);

        if (Status.confirmed.name().equals(userTask.getStatus())) {
            service.eventConfirmedTask(userTask);
        }

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

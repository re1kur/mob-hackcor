package re1kur.uas.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import re1kur.core.dto.UserTaskDto;
import re1kur.core.exception.StatusUpdateFailedException;
import re1kur.core.exception.UserTaskNotFoundException;
import re1kur.uas.entity.UserTask;
import re1kur.uas.entity.UserTaskId;
import re1kur.uas.enums.Status;
import re1kur.uas.mapper.UserTaskMapper;
import re1kur.uas.repository.UserTaskRepository;
import re1kur.uas.service.EventService;
import re1kur.uas.service.UserTaskService;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserTaskServiceImpl implements UserTaskService {
    private final UserTaskRepository repo;
    private final UserTaskMapper mapper;
    private final EventService service;

    @Override
    @Transactional
    public void updateStatus(String userId, Long taskId, String status) {
        UUID uuid = UUID.fromString(userId);
        Integer i = repo.updateStatus(uuid, taskId, status);
        if (i < 0) throw new StatusUpdateFailedException("FAILED TO UPDATE STATUS USER TASK");
        if (Status.confirmed.name().equals(status))
            service.eventConfirmedTask(repo.findById(new UserTaskId(uuid, taskId)).get());
    }

    @Override
    public List<UserTaskDto> getAllByUser(String userId) {
        return repo.findAllByIdUserId(UUID.fromString(userId)).stream()
                .map(mapper::read)
                .toList();
    }

    @Override
    public UserTaskDto getById(String userId, Long taskId) {
        return repo.findById(UserTaskId.builder()
                        .userId(UUID.fromString(userId))
                        .taskId(taskId)
                        .build())
                .map(mapper::read)
                .orElseThrow(() -> new UserTaskNotFoundException(
                        "Task %s was not found for the %s user.".formatted(userId, taskId)));
    }

    @Override
    public void delete(String userId, Long taskId) {
        UserTask userTask = repo.findById(UserTaskId.builder()
                        .userId(UUID.fromString(userId))
                        .taskId(taskId)
                        .build())
                .orElseThrow(() -> new UserTaskNotFoundException(
                        "Task %s was not found for the %s user.".formatted(userId, taskId)));
        repo.delete(userTask);
    }
}

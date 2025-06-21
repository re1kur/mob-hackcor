package re1kur.uas.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import re1kur.core.dto.UserTaskDto;
import re1kur.core.exception.UserTaskNotFoundException;
import re1kur.uas.entity.UserTask;
import re1kur.uas.entity.UserTaskId;
import re1kur.uas.mapper.UserTaskMapper;
import re1kur.uas.repository.UserTaskRepository;
import re1kur.uas.service.UserTaskService;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserTaskServiceImpl implements UserTaskService {
    private final UserTaskRepository repo;
    private final UserTaskMapper mapper;

    @Override
    @Transactional
    public UserTaskDto updateStatus(String userId, Long taskId, String status) {
        return mapper.read(repo.updateStatus(UUID.fromString(userId), taskId, status));
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

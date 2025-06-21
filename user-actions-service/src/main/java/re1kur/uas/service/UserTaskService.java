package re1kur.uas.service;

import re1kur.core.dto.UserTaskDto;

import java.util.List;

public interface UserTaskService {
    void updateStatus(String userId, Long taskId, String status);

    List<UserTaskDto> getAllByUser(String userId);

    UserTaskDto getById(String userId, Long taskId);

    void delete(String userId, Long taskId);
}

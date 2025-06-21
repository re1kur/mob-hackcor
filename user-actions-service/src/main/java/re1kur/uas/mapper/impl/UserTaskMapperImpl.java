package re1kur.uas.mapper.impl;

import org.springframework.stereotype.Component;
import re1kur.core.dto.UserTaskDto;
import re1kur.uas.entity.TaskAttempt;
import re1kur.uas.entity.UserTaskId;
import re1kur.uas.entity.UserTask;
import re1kur.uas.mapper.UserTaskMapper;

@Component
public class UserTaskMapperImpl implements UserTaskMapper {
    @Override
    public UserTask write(TaskAttempt attempt) {
        UserTaskId userTaskId = UserTaskId.builder()
                .userId(attempt.getUserId())
                .taskId(attempt.getTask().getId())
                .build();
        return UserTask.builder()
                .id(userTaskId)
                .lastAttempt(attempt)
                .build();
    }

    @Override
    public UserTaskDto read(UserTask task) {
        return UserTaskDto.builder()
                .userId(task.getId().getUserId().toString())
                .taskId(task.getId().getTaskId())
                .lastAttemptId(
                        task.getLastAttempt() != null ? task.getLastAttempt().getId() : null
                )
                .status(task.getStatus())
                .build();

    }
}

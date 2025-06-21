package re1kur.uas.mapper;

import re1kur.core.dto.UserTaskDto;
import re1kur.uas.entity.TaskAttempt;
import re1kur.uas.entity.UserTask;

public interface UserTaskMapper {
    UserTask write(TaskAttempt attempt);

    UserTaskDto read(UserTask userTask);
}

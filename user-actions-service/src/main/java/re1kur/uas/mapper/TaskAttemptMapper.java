package re1kur.uas.mapper;

import re1kur.core.dto.TaskAttemptDto;
import re1kur.core.payload.TaskAttemptPayload;
import re1kur.core.payload.TaskAttemptUpdatePayload;
import re1kur.uas.entity.TaskAttempt;

public interface TaskAttemptMapper {
    TaskAttempt write(TaskAttemptPayload payload);

    TaskAttemptDto read(TaskAttempt attempt);

    TaskAttempt update(TaskAttempt attempt, TaskAttemptUpdatePayload payload);
}

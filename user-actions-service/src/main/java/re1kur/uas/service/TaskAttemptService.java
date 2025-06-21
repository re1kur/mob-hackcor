package re1kur.uas.service;

import re1kur.core.dto.TaskAttemptDto;
import re1kur.core.payload.TaskAttemptPayload;
import re1kur.core.payload.TaskAttemptUpdatePayload;

public interface TaskAttemptService {
    TaskAttemptDto create(TaskAttemptPayload payload);

    TaskAttemptDto getById(Long id);

    TaskAttemptDto update(TaskAttemptUpdatePayload payload);

    void delete(Long id);
}

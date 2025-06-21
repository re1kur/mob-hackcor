package re1kur.uas.mapper.impl;

import org.springframework.stereotype.Component;
import re1kur.core.dto.TaskAttemptDto;
import re1kur.core.payload.TaskAttemptPayload;
import re1kur.core.payload.TaskAttemptUpdatePayload;
import re1kur.uas.entity.TaskAttempt;
import re1kur.uas.mapper.TaskAttemptMapper;

import java.util.UUID;

@Component
public class TaskAttemptMapperImpl implements TaskAttemptMapper {
    @Override
    public TaskAttempt write(TaskAttemptPayload payload) {
        return TaskAttempt.builder()
                .userId(UUID.fromString(payload.userId()))
                .textContent(payload.textContent())
                .fileContentId(payload.fileContentId() != null ? UUID.fromString(payload.fileContentId()) : null)
                .confirmed(null)
                .build();
    }

    @Override
    public TaskAttemptDto read(TaskAttempt attempt) {
        return TaskAttemptDto.builder()
                .id(attempt.getId())
                .userId(attempt.getUserId().toString())
                .taskId(attempt.getTask().getId())
                .attemptTime(attempt.getAttemptTime())
                .textContent(attempt.getTextContent())
                .fileContentId(attempt.getFileContentId() != null ? attempt.getFileContentId().toString() : null)
                .confirmed(attempt.getConfirmed())
                .moderatorId(attempt.getModeratorId() != null ? attempt.getModeratorId().toString() : null)
                .build();
    }

    @Override
    public TaskAttempt update(TaskAttempt attempt, TaskAttemptUpdatePayload payload) {
        if (payload.textContent() != null)
            attempt.setTextContent(payload.textContent());
        if (payload.fileContentId() != null)
            attempt.setFileContentId(UUID.fromString(payload.fileContentId()));
        if (payload.confirmed() != null)
            attempt.setConfirmed(payload.confirmed());
        if (payload.moderatorId() != null)
            attempt.setModeratorId(UUID.fromString(payload.moderatorId()));
        if (payload.attemptTime() != null)
            attempt.setAttemptTime(payload.attemptTime());
        return attempt;
    }
}

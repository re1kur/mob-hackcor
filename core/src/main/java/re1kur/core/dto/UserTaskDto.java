package re1kur.core.dto;

import lombok.Builder;

@Builder
public record UserTaskDto(
        String userId,
        Long taskId,
        Long lastAttemptId,
        String status
) {}

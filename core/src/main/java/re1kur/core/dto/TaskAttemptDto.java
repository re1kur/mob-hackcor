package re1kur.core.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record TaskAttemptDto(
        Long id,
        String userId,
        Long dailyTaskId,
        LocalDateTime attemptTime,
        String textContent,
        String fileContentId,
        Boolean confirmed,
        String moderatorId
) {
}

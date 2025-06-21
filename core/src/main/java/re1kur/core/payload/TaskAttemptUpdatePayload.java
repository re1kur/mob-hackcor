package re1kur.core.payload;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record TaskAttemptUpdatePayload(
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

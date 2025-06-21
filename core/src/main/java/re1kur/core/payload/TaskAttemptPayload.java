package re1kur.core.payload;

import lombok.Builder;

@Builder
public record TaskAttemptPayload(
        String userId,
        Long dailyTaskId,
        String textContent,
        String fileContentId
) {
}

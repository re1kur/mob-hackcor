package re1kur.core.payload;

import lombok.Builder;

@Builder
public record TaskAttemptPayload(
        String userId,
        Long taskId,
        String textContent,
        String fileContentId
) {
}

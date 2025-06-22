package re1kur.core.payload;

import lombok.Builder;

@Builder
public record TaskAttemptPayload(
        Long taskId,
        String textContent,
        String fileContentId
) {
}

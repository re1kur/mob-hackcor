package re1kur.core.payload;

import jakarta.validation.constraints.*;
import lombok.Builder;

import java.time.OffsetDateTime;

@Builder
public record TaskPayload(
        @NotBlank(message = "Title must be not empty.")
        @Size(min = 4, max = 64, message = "Title must be between 4 and 64 characters long.")
        String title,
        String shortDescription,
        @NotBlank(message = "Description must be not empty.")
        String description,
        @FutureOrPresent(message = "Start date must be between the present and future.")
        OffsetDateTime startsAt,
        @Future(message = "End date can not be in past.")
        OffsetDateTime endsAt,
        @Positive(message = "Reward must be positive.")
        Integer reward,
        String imageFileId,
        String imageFileUrl,
        OffsetDateTime urlExpiresAt
) {
}

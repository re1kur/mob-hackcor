package re1kur.core.dto;

import lombok.Builder;

import java.time.OffsetDateTime;

@Builder
public record TaskDto(
        Long id,
        String title,
        String shortDescription,
        String description,
        OffsetDateTime startsAt,
        OffsetDateTime endsAt,
        String imageUrl,
        OffsetDateTime expiresAt,
        Integer reward
) {
}

package re1kur.core.dto;

import lombok.Builder;

import java.time.ZonedDateTime;

@Builder
public record FileDto(
        String id,
        String extension,
        String url,
        ZonedDateTime uploadedAt,
        ZonedDateTime urlExpiresAt
) {
}

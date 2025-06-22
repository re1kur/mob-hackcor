package re1kur.core.dto;

import lombok.Builder;

import java.util.UUID;

@Builder
public record CourseDto(
        UUID id,
        String title,
        String description
) {
}

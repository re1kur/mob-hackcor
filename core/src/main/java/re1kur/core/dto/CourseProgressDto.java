package re1kur.core.dto;

import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record CourseProgressDto(
        UUID courseId,
        Integer score,
        Boolean completed,
        Boolean earnedCertificate,
        Integer earnedPoints,
        Instant completedAt
) {
}

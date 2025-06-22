package re1kur.core.dto;

import lombok.Builder;

import java.util.UUID;

@Builder
public record LectureDto(
        UUID id,
        String title,
        String content,
        Integer order,
        UUID courseId
) {
}

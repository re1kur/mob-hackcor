package re1kur.core.payload;

import lombok.Builder;

import java.util.UUID;

@Builder
public record LecturePayload(
        String title,
        String content,
        Integer orderIndex,
        UUID courseId
) {
}

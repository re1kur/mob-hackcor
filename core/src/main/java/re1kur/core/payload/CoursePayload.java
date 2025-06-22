package re1kur.core.payload;

import lombok.Builder;

@Builder
public record CoursePayload(
        String title,
        String description
) {
}

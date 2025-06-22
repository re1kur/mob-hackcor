package re1kur.core.payload;

import lombok.Builder;

import java.util.List;
import java.util.UUID;

@Builder
public record QuizPayload(
        UUID courseId,
        String title,
        List<QuestionPayload> questions
) {
}

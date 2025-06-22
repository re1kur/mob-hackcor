package re1kur.core.payload;

import lombok.Builder;

import java.util.List;

@Builder
public record QuestionPayload(
        String text,
        List<String> options,
        Integer correctOptionIndex
) {
}

package re1kur.core.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record QuestionDto (
        String id,
        String text,
        List<String> options
) {
}

package re1kur.core.dto;

import lombok.Builder;

import java.util.UUID;

@Builder
public record FlashcardDto (
        UUID id,
        String question,
        String answer
) {
}

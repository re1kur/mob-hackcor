package re1kur.core;

import lombok.Builder;

import java.util.UUID;

@Builder
public record FlashcardDto (
        UUID id,
        String question,
        String answer
) {
}

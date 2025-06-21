package re1kur.core.dto;

import lombok.Builder;

@Builder
public record TaskDto(
        Long id,
        String title,
        String description,
        Integer reward
) {
}

package re1kur.core.dto;

import lombok.Builder;

@Builder
public record DailyTaskDto(
        Long id,
        String title,
        String description,
        Integer reward
) {
}

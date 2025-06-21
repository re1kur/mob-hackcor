package re1kur.core.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record DailyTaskPayload(
        @NotBlank(message = "Title must be not empty.")
        @Size(min = 4, max = 64)
        String title,
        @NotBlank(message = "Description must be not empty.")
        String description,
        @Positive(message = "Reward must be positive.")
        Integer reward
) {
}

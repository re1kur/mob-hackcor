package re1kur.core.payload;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record LoginRequest(
        @NotBlank(message = "Email не может быть пустым") String email,
        @NotBlank(message = "Пароль не может быть пустым") String password
) {
}

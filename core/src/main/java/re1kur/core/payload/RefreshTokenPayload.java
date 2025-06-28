package re1kur.core.payload;

import jakarta.validation.constraints.NotBlank;

public record RefreshTokenPayload(
        @NotBlank(message = "Refresh token can not be empty or contain backspaces.")
        String refreshToken
) {
}

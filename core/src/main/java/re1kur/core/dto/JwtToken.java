package re1kur.core.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record JwtToken(
        String accessToken,
        String refreshToken,
        LocalDateTime expiresAt,
        LocalDateTime refreshExpiresAt
) {
}

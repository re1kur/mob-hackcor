package re1kur.ums.jwt;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record JwtToken(
        String body,
        String refreshToken,
        LocalDateTime expiresAt
) {
}

package re1kur.ums.jwt;

import lombok.Builder;

import java.time.Instant;

@Builder
public record JwtToken(
        String body,
        String refreshToken,
        Instant expiresAt,
        Instant refreshExpiresAt
) {
}

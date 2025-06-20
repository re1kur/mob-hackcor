package re1kur.ums.jwt;

import java.util.UUID;

public interface JwtProvider {
    JwtToken getToken(UUID id);
}

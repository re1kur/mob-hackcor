package re1kur.ums.service.impl;

import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTParser;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import re1kur.core.exception.*;
import re1kur.ums.entity.RefreshToken;
import re1kur.ums.entity.User;
import re1kur.ums.jwt.JwtProvider;
import re1kur.core.dto.JwtToken;
import re1kur.ums.repository.redis.TokenRepository;
import re1kur.ums.repository.sql.UserRepository;
import re1kur.ums.service.TokenService;

import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {
    private final JwtProvider jwtProvider;
    private final TokenRepository repo;
    private final UserRepository userRepo;

    @Value("${custom.jwt.publicKeyPath}")
    private String publicKeyPath;

    @Value("${custom.jwt.kidPath}")
    private String kidPath;

    @Override
    public JwtToken refreshToken(String refreshToken) throws ParseException {
        String userIdStr = verify(refreshToken);

        RefreshToken token = repo.findById(userIdStr).orElseThrow(() ->
                new TokenNotFoundException("Token for user %s not found.".formatted(userIdStr)));
        if (!token.getBody().equals(refreshToken)) {
            throw new TokenMismatchException("Refresh token mismatch");
        }

        UUID userId = UUID.fromString(userIdStr);
        User user = userRepo.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));

        return jwtProvider.getToken(user);
    }

    private String verify(String refreshToken) throws ParseException {
        JWT parsed = JWTParser.parse(refreshToken);
        String userIdStr = parsed.getJWTClaimsSet().getSubject();
        Instant expiration = parsed.getJWTClaimsSet().getExpirationTime().toInstant();
        if (!jwtProvider.verifySignature(parsed)) {
            throw new TokenDidNotPassVerificationException("Token did not pass verification.");
        }
        if (Instant.now().isAfter(expiration)) {
            throw new TokenHasExpiredException("Token has expired. Authenticate again.");
        }
        return userIdStr;
    }

    @Override
    public Map<String, Object> getPublicKey() {
        String kid = jwtProvider.readKidFromFile(kidPath);
        RSAPublicKey publicKey = jwtProvider.readPublicKeyFromFile(publicKeyPath);

        RSAKey publicJwk = new RSAKey.Builder(publicKey)
                .keyID(kid)
                .build();
        return Map.of("keys", List.of(publicJwk.toPublicJWK().toJSONObject()));
    }
}

package re1kur.ums.service.impl;

import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTParser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import re1kur.core.exception.InvalidTokenException;
import re1kur.core.exception.TokenMismatchException;
import re1kur.core.exception.TokenNotFoundException;
import re1kur.core.exception.UserNotFoundException;
import re1kur.ums.entity.RefreshToken;
import re1kur.ums.entity.User;
import re1kur.ums.jwt.JwtProvider;
import re1kur.ums.jwt.JwtToken;
import re1kur.ums.repository.redis.TokenRepository;
import re1kur.ums.repository.sql.UserRepository;
import re1kur.ums.service.TokenService;

import java.text.ParseException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {
    private final JwtProvider jwtProvider;
    private final TokenRepository repo;
    private final UserRepository userRepo;

    @Override
    public JwtToken refreshToken(String refreshToken) throws ParseException {
        JWT parsed = JWTParser.parse(refreshToken);
        if (!jwtProvider.verifyToken(parsed)) {
            throw new InvalidTokenException("Invalid refresh token");
        }

        String userIdStr = parsed.getJWTClaimsSet().getSubject();

        RefreshToken token = repo.findById(userIdStr).orElseThrow(() -> new TokenNotFoundException("Token for user %s not found.".formatted(userIdStr)));

        if (!token.getBody().equals(refreshToken)) {
            throw new TokenMismatchException("Refresh token mismatch");
        }
        UUID userId = UUID.fromString(userIdStr);
        User user = userRepo.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));

        return jwtProvider.getToken(user);
    }
}

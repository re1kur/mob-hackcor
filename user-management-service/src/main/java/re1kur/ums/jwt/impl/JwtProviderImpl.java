package re1kur.ums.jwt.impl;

import org.springframework.stereotype.Component;
import re1kur.ums.jwt.JwtProvider;
import re1kur.ums.jwt.JwtToken;

import java.util.UUID;

@Component
public class JwtProviderImpl implements JwtProvider {
    @Override
    public JwtToken getToken(UUID id) {
        return null;
    }
}

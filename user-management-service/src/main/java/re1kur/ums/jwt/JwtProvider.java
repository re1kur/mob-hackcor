package re1kur.ums.jwt;

import com.nimbusds.jwt.JWT;
import re1kur.ums.entity.User;

public interface JwtProvider {
    JwtToken getToken(User user);

    boolean verifyToken(JWT refreshToken);
}

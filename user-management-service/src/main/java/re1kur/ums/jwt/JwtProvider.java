package re1kur.ums.jwt;

import re1kur.ums.entity.User;

public interface JwtProvider {
    JwtToken getToken(User user);
}

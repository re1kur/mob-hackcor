package re1kur.ums.jwt;

import com.nimbusds.jwt.JWT;
import re1kur.core.dto.JwtToken;
import re1kur.ums.entity.User;

import java.security.interfaces.RSAPublicKey;

public interface JwtProvider {
    JwtToken getToken(User user);

    boolean verifySignature(JWT refreshToken);

    String readKidFromFile(String path);

    RSAPublicKey readPublicKeyFromFile(String path);

}

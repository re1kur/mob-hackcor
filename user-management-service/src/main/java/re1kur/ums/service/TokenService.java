package re1kur.ums.service;

import re1kur.core.dto.JwtToken;

import java.text.ParseException;
import java.util.Map;

public interface TokenService {
    JwtToken refreshToken(String refreshToken) throws ParseException;

    Map<String, Object> getPublicKey() throws Exception;
}

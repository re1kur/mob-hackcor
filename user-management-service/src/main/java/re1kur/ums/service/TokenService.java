package re1kur.ums.service;

import re1kur.ums.jwt.JwtToken;

import java.text.ParseException;

public interface TokenService {
    JwtToken refreshToken(String refreshToken) throws ParseException;
}

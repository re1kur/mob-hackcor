package re1kur.ums.service;

import re1kur.core.exception.UserAlreadyRegisteredException;
import re1kur.core.exception.UserNotFoundException;
import re1kur.core.payload.LoginRequest;
import re1kur.core.payload.UserPayload;
import re1kur.core.dto.JwtToken;

import java.util.UUID;

public interface AuthService {
    UUID register(UserPayload payload) throws UserAlreadyRegisteredException;

    JwtToken login(LoginRequest request) throws UserNotFoundException;
}

package re1kur.ums.service;

import re1kur.core.dto.UserDto;
import re1kur.core.exception.UserAlreadyRegisteredException;
import re1kur.core.exception.UserNotFoundException;
import re1kur.core.payload.LoginRequest;
import re1kur.core.payload.UserPayload;
import re1kur.ums.jwt.JwtToken;

public interface AuthService {
    UserDto register(UserPayload payload) throws UserAlreadyRegisteredException;

    JwtToken login(LoginRequest request) throws UserNotFoundException;
}

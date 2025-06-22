package re1kur.ums.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import re1kur.core.dto.UserDto;
import re1kur.core.exception.InvalidCredentialsException;
import re1kur.core.exception.UserAlreadyRegisteredException;
import re1kur.core.exception.UserNotFoundException;
import re1kur.core.payload.LoginRequest;
import re1kur.core.payload.UserPayload;
import re1kur.ums.entity.UserInformation;
import re1kur.ums.jwt.JwtProvider;
import re1kur.ums.jwt.JwtToken;
import re1kur.ums.entity.User;
import re1kur.ums.mapper.UserMapper;
import re1kur.ums.repository.sql.UserRepository;
import re1kur.ums.service.AuthService;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository repo;
    private final UserMapper mapper;
    private final BCryptPasswordEncoder encoder;
    private final JwtProvider jwtProvider;

    @Override
    public UserDto register(UserPayload payload) {
        if (repo.existsByEmail(payload.email()))
            throw new UserAlreadyRegisteredException(
                    "User with email %s already registered.".formatted(payload.email()));
        User mapped = mapper.write(payload);
        User saved = repo.save(mapped);
        UserInformation info = UserInformation.builder()
                .firstname(payload.firstname())
                .lastname(payload.lastname())
                .user(saved)
                .build();
        saved.setInformation(info);
        return mapper.read(saved);
    }

    @Override
    public JwtToken login(LoginRequest request) {
        User user = repo.findByEmail(request.email())
                .orElseThrow(() -> new UserNotFoundException(
                        "User with email %s not exist.".formatted(request.email())));
        if (!encoder.matches(request.password(), user.getPassword()))
            throw new InvalidCredentialsException("Invalid password.");
        return jwtProvider.getToken(user);
    }
}

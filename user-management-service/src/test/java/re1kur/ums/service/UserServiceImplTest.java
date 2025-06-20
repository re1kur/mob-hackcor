package re1kur.ums.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import re1kur.core.dto.UserDto;
import re1kur.core.exception.InvalidCredentialsException;
import re1kur.core.exception.UserAlreadyRegisteredException;
import re1kur.core.exception.UserNotFoundException;
import re1kur.core.payload.LoginRequest;
import re1kur.core.payload.UserPayload;
import re1kur.ums.jwt.JwtProvider;
import re1kur.ums.jwt.JwtToken;
import re1kur.ums.entity.User;
import re1kur.ums.mapper.UserMapper;
import re1kur.ums.repository.UserRepository;
import re1kur.ums.service.impl.UserServiceImpl;

import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @InjectMocks
    private UserServiceImpl service;

    @Mock
    private UserRepository repo;

    @Mock
    private UserMapper mapper;

    @Mock
    private BCryptPasswordEncoder encoder;

    @Mock
    private JwtProvider jwtProvider;

    @Test
    void testRegister__ValidUser__DoesNotThrowException() {
        UserPayload payload = UserPayload.builder()
                .email("email@email.com")
                .password("password")
                .build();
        User mapped = User.builder()
                .email("email@email.com")
                .password("password")
                .build();
        User saved = User.builder()
                .id(UUID.fromString("bdc76ed5-c3b5-4efd-b2cd-1cb3e244c676"))
                .email("email@email.com")
                .password("password")
                .build();
        UserDto expected = UserDto.builder()
                .id("bdc76ed5-c3b5-4efd-b2cd-1cb3e244c676")
                .email("email@example.com")
                .enabled(false)
                .build();

        Mockito.when(repo.existsByEmail(Mockito.anyString())).thenReturn(false);
        Mockito.when(mapper.write(Mockito.any(UserPayload.class))).thenReturn(mapped);
        Mockito.when(repo.save(Mockito.any(User.class))).thenReturn(saved);
        Mockito.when(mapper.read(Mockito.any(User.class))).thenReturn(expected);

        UserDto result = Assertions.assertDoesNotThrow(() -> service.register(payload));
        Assertions.assertEquals(expected, result);

        Mockito.verify(repo, Mockito.times(1)).existsByEmail(Mockito.anyString());
        Mockito.verify(mapper, Mockito.times(1)).write(Mockito.any(UserPayload.class));
        Mockito.verify(repo, Mockito.times(1)).save(Mockito.any(User.class));
        Mockito.verify(mapper, Mockito.times(1)).read(Mockito.any(User.class));
    }

    @Test
    void testRegister__AlreadyRegisteredUser__ThrowUserAlreadyRegisteredException() {
        UserPayload payload = UserPayload.builder()
                .email("email@email.com")
                .password("password")
                .build();

        Mockito.when(repo.existsByEmail(Mockito.anyString())).thenReturn(true);

        Assertions.assertThrows(UserAlreadyRegisteredException.class, () -> service.register(payload));

        Mockito.verify(repo, Mockito.times(1)).existsByEmail(Mockito.anyString());
        Mockito.verifyNoMoreInteractions(repo);
        Mockito.verifyNoInteractions(mapper);
    }

    @Test
    void testLogin__ValidUser__DoesNotThrowException() {
        LoginRequest request = LoginRequest.builder()
                .email("email@example.com")
                .password("password")
                .build();
        User user = User.builder()
                .id(UUID.fromString("bdc76ed5-c3b5-4efd-b2cd-1cb3e244c676"))
                .email("email@example.com")
                .password("encodedPassword")
                .enabled(true)
                .build();
        JwtToken expected = JwtToken.builder()
                .body("eyJaHeader.payload.signature")
                .build();

        Mockito.when(repo.findByEmail("email@example.com")).thenReturn(Optional.of(user));
        Mockito.when(encoder.matches("password", "encodedPassword")).thenReturn(true);
        Mockito.when(jwtProvider.getToken(user)).thenReturn(expected);

        JwtToken result = Assertions.assertDoesNotThrow(() -> service.login(request));
        Assertions.assertEquals(expected, result);

        Mockito.verify(repo, Mockito.times(1)).findByEmail("email@example.com");
        Mockito.verify(encoder, Mockito.times(1)).matches("password", "encodedPassword");
        Mockito.verify(jwtProvider, Mockito.times(1)).getToken(user);
    }

    @Test
    void testLogin__UserNotFound__ThrowUserNotFoundException() {
        LoginRequest request = LoginRequest.builder()
                .email("email@example.com")
                .password("password")
                .build();

        Mockito.when(repo.findByEmail("email@example.com")).thenReturn(Optional.empty());

        Assertions.assertThrows(UserNotFoundException.class, () -> service.login(request));

        Mockito.verify(repo, Mockito.times(1)).findByEmail("email@example.com");
        Mockito.verifyNoInteractions(encoder, jwtProvider);
    }

    @Test
    void testLogin__UserExistsButInvalidCredentials__ThrowInvalidCredentialsException() {
        LoginRequest request = LoginRequest.builder()
                .email("email@example.com")
                .password("password")
                .build();
        User user = User.builder()
                .id(UUID.fromString("bdc76ed5-c3b5-4efd-b2cd-1cb3e244c676"))
                .email("email@example.com")
                .password("encodedPassword")
                .enabled(true)
                .build();

        Mockito.when(repo.findByEmail("email@example.com")).thenReturn(Optional.of(user));
        Mockito.when(encoder.matches("password", "encodedPassword")).thenReturn(false);

        Assertions.assertThrows(InvalidCredentialsException.class, () -> service.login(request));

        Mockito.verify(repo, Mockito.times(1)).findByEmail("email@example.com");
        Mockito.verify(encoder, Mockito.times(1)).matches("password", "encodedPassword");
        Mockito.verifyNoInteractions(jwtProvider);
    }
}
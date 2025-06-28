package re1kur.ums.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import re1kur.core.exception.*;
import re1kur.core.other.CodeGenerator;
import re1kur.core.payload.EmailVerificationPayload;
import re1kur.ums.entity.Code;
import re1kur.ums.entity.User;
import re1kur.ums.mapper.CodeMapper;
import re1kur.ums.repository.redis.CodeRepository;
import re1kur.ums.repository.sql.UserRepository;
import re1kur.ums.service.impl.VerificationServiceImpl;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class VerificationServiceTest {
    @InjectMocks
    private VerificationServiceImpl service;

    @Mock
    private UserRepository userRepo;

    @Mock
    private CodeRepository codeRepo;

    @Mock
    private CodeMapper mapper;

    private static UUID id;
    private User user;

    @BeforeEach
     void setUp() {
        id = UUID.randomUUID();
        user = User.builder()
                .id(id)
                .enabled(false)
                .build();
    }

    @Test
    void testVerifyEmail__UserExists__DoesNotThrowException() {
        EmailVerificationPayload payload = EmailVerificationPayload.builder()
                .email("email@example.com")
                .code("123123")
                .build();
        Code expected = Code.builder()
                .id(id.toString())
                .value("123123")
                .expiresAt(Instant.now().plusSeconds(100))
                .issuedAt(Instant.now())
                .build();

        Mockito.when(userRepo.findByEmail("email@example.com")).thenReturn(Optional.of(user));
        Mockito.when(codeRepo.findById(user.getId().toString())).thenReturn(Optional.of(expected));

        Assertions.assertDoesNotThrow(() -> service.verifyEmail(payload));

        Mockito.verify(userRepo, Mockito.times(1)).findByEmail("email@example.com");
        Mockito.verify(codeRepo, Mockito.times(1)).findById(user.getId().toString());
    }

    @Test
    void testVerifyEmail__UserNotExists__ThrowsUserNotFoundException() {
        EmailVerificationPayload payload = EmailVerificationPayload.builder()
                .email("email@example.com")
                .code("123123")
                .build();

        Mockito.when(userRepo.findByEmail("email@example.com")).thenReturn(Optional.empty());

        Assertions.assertThrows(UserNotFoundException.class, () -> service.verifyEmail(payload));

        Mockito.verify(userRepo, Mockito.times(1)).findByEmail("email@example.com");
        Mockito.verifyNoInteractions(codeRepo);
    }

    @Test
    void testVerifyEmail__CodeNotExists__ThrowsCodeNotFoundException() {
        EmailVerificationPayload payload = EmailVerificationPayload.builder()
                .email("email@example.com")
                .code("123123")
                .build();

        Mockito.when(userRepo.findByEmail("email@example.com")).thenReturn(Optional.of(user));
        Mockito.when(codeRepo.findById(user.getId().toString())).thenReturn(Optional.empty());

        Assertions.assertThrows(CodeNotFoundException.class, () -> service.verifyEmail(payload));

        Mockito.verify(userRepo, Mockito.times(1)).findByEmail("email@example.com");
        Mockito.verify(codeRepo, Mockito.times(1)).findById(user.getId().toString());
    }

    @Test
    void testVerifyEmail__CodeMismatch__ThrowsCodeMismatchException() {
        EmailVerificationPayload payload = EmailVerificationPayload.builder()
                .email("email@example.com")
                .code("123123")
                .build();
        Code expected = Code.builder()
                .id(id.toString())
                .value("312412")
                .expiresAt(Instant.now().plusSeconds(100))
                .issuedAt(Instant.now())
                .build();

        Mockito.when(userRepo.findByEmail("email@example.com")).thenReturn(Optional.of(user));
        Mockito.when(codeRepo.findById(user.getId().toString())).thenReturn(Optional.of(expected));

        Assertions.assertThrows(CodeMismatchException.class, () -> service.verifyEmail(payload));

        Mockito.verify(userRepo, Mockito.times(1)).findByEmail("email@example.com");
        Mockito.verify(codeRepo, Mockito.times(1)).findById(user.getId().toString());
    }

    @Test
    void testVerifyEmail__CodeHasExpired__ThrowsCodeHasExpiredException() {
        EmailVerificationPayload payload = EmailVerificationPayload.builder()
                .email("email@example.com")
                .code("123123")
                .build();

        Code expected = Code.builder()
                .id(id.toString())
                .value("123123")
                .expiresAt(Instant.now().minusSeconds(100))
                .issuedAt(Instant.now().minusSeconds(200))
                .build();

        Mockito.when(userRepo.findByEmail("email@example.com")).thenReturn(Optional.of(user));
        Mockito.when(codeRepo.findById(user.getId().toString())).thenReturn(Optional.of(expected));

        Assertions.assertThrows(CodeHasExpiredException.class, () -> service.verifyEmail(payload));

        Mockito.verify(userRepo, Mockito.times(1)).findByEmail("email@example.com");
        Mockito.verify(codeRepo, Mockito.times(1)).findById(user.getId().toString());
    }

    @Test
    void testVerifyEmail__UserAlreadyVerified__ThrowsUserAlreadyVerifiedException() {
        EmailVerificationPayload payload = EmailVerificationPayload.builder()
                .email("email@example.com")
                .code("123123")
                .build();

        user = User.builder()
                .id(id)
                .enabled(true)
                .build();

        Mockito.when(userRepo.findByEmail("email@example.com")).thenReturn(Optional.of(user));

        Assertions.assertThrows(UserAlreadyVerifiedException.class, () -> service.verifyEmail(payload));

        Mockito.verify(userRepo, Mockito.times(1)).findByEmail("email@example.com");
        Mockito.verifyNoInteractions(codeRepo);
    }

    @Test
    void testGenerateCode__UserExists__GeneratesAndSavesCode() {
        String email = "email@example.com";
        UUID userId = UUID.randomUUID();
        user = User.builder()
                .id(userId)
                .email(email)
                .build();
        String generatedCode = "123456";
        Code mappedCode = Code.builder()
                .id(userId.toString())
                .value(generatedCode)
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(300))
                .build();

        Mockito.mockStatic(CodeGenerator.class).when(CodeGenerator::generateOTP).thenReturn(generatedCode);
        Mockito.when(userRepo.findByEmail(email)).thenReturn(Optional.of(user));
        Mockito.when(mapper.write(userId.toString(), generatedCode)).thenReturn(mappedCode);

        service.generateCode(email);

        Mockito.verify(userRepo, Mockito.times(1)).findByEmail(email);
        Mockito.verify(mapper, Mockito.times(1)).write(userId.toString(), generatedCode);
        Mockito.verify(codeRepo, Mockito.times(1)).save(mappedCode);
    }

    @Test
    void testGenerateCode__UserNotExists__ThrowsUserNotFoundException() {
        String email = "email@example.com";
        Mockito.when(userRepo.findByEmail(email)).thenReturn(Optional.empty());

        Assertions.assertThrows(UserNotFoundException.class, () -> service.generateCode(email));

        Mockito.verify(userRepo, Mockito.times(1)).findByEmail(email);
        Mockito.verifyNoInteractions(mapper);
        Mockito.verifyNoInteractions(codeRepo);
    }

}
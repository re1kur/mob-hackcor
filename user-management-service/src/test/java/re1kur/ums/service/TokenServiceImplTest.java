//package re1kur.ums.service;
//
//import com.nimbusds.jwt.*;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.junit.jupiter.MockitoExtension;
//import re1kur.core.exception.TokenDidNotPassVerificationException;
//import re1kur.core.exception.TokenMismatchException;
//import re1kur.core.exception.TokenNotFoundException;
//import re1kur.core.exception.UserNotFoundException;
//import re1kur.ums.entity.RefreshToken;
//import re1kur.ums.entity.User;
//import re1kur.ums.jwt.JwtProvider;
//import re1kur.core.dto.JwtToken;
//import re1kur.ums.repository.redis.TokenRepository;
//import re1kur.ums.repository.sql.UserRepository;
//import re1kur.ums.service.impl.TokenServiceImpl;
//
//import java.text.ParseException;
//import java.util.Optional;
//import java.util.UUID;
//
//@ExtendWith(MockitoExtension.class)
//class TokenServiceImplTest {
//    @InjectMocks
//    private TokenServiceImpl service;
//    @Mock
//    private JwtProvider jwtProvider;
//    @Mock
//    private TokenRepository repo;
//    @Mock
//    private UserRepository userRepo;
//
//    private static final String refreshToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3OC0xMjM0LTU2NzgtMTIzNC01Njc4MTIzNDU2NzgiLCJuYW1lIjoiSm9obiBEb2UiLCJhZG1pbiI6dHJ1ZSwiaWF0IjoxNTE2MjM5MDIyfQ.Q1vQ0T286WWYdWT6YrnNr-unpNUAkfUy2rFv4Q9JJ0Y";
//
//    @Test
//    void testRefreshToken__ValidToken__DoesNotThrowException() {
//        UUID id = Mockito.mock(UUID.class);
//        User user = Mockito.mock(User.class);
//        RefreshToken token = RefreshToken.builder()
//                .id("12345678-1234-5678-1234-567812345678")
//                .body(refreshToken)
//                .build();
//        JwtToken expected = JwtToken.builder()
//                .refreshToken(refreshToken)
//                .accessToken("body")
//                .build();
//
//
//        Mockito.when(jwtProvider.verifySignature(Mockito.any(JWT.class))).thenReturn(true);
//        Mockito.when(repo.findById("12345678-1234-5678-1234-567812345678")).thenReturn(Optional.of(token));
//        Mockito.when(userRepo.findById(UUID.fromString("12345678-1234-5678-1234-567812345678"))).thenReturn(Optional.of(user));
//        Mockito.when(jwtProvider.getToken(user)).thenReturn(expected);
//
//        JwtToken result = Assertions.assertDoesNotThrow(() -> service.refreshToken(refreshToken));
//        Assertions.assertEquals(expected, result);
//
//        Mockito.verify(jwtProvider, Mockito.times(1)).verifySignature(Mockito.any(JWT.class));
//        Mockito.verify(userRepo, Mockito.times(1)).findById(UUID.fromString("12345678-1234-5678-1234-567812345678"));
//    }
//
//    @Test
//    void testRefreshToken__NotParsingToken__ThrowParseException() {
//        String invalidRefreshToken = "invalid";
//        Assertions.assertThrows(ParseException.class, () -> service.refreshToken(invalidRefreshToken));
//
//        Mockito.verifyNoInteractions(jwtProvider, userRepo, repo);
//    }
//
//    @Test
//    void testRefreshToken__InvalidToken__ThrowInvalidTokenException() {
//        Mockito.when(jwtProvider.verifySignature(Mockito.any(JWT.class))).thenReturn(false);
//
//        Assertions.assertThrows(TokenDidNotPassVerificationException.class, () -> service.refreshToken(refreshToken));
//
//        Mockito.verifyNoInteractions(userRepo, repo);
//    }
//
//    @Test
//    void testRefreshToken__NotExistingToken__ThrowTokenNotFoundException() {
//        Mockito.when(jwtProvider.verifySignature(Mockito.any(JWT.class))).thenReturn(true);
//        Mockito.when(repo.findById("12345678-1234-5678-1234-567812345678")).thenReturn(Optional.empty());
//
//        Assertions.assertThrows(TokenNotFoundException.class, () -> service.refreshToken(refreshToken));
//
//        Mockito.verifyNoInteractions(userRepo);
//    }
//
//    @Test
//    void testRefreshToken__MismatchedToken__ThrowTokenMismatchException() {
//        RefreshToken token = RefreshToken.builder()
//                .id("12345678-1234-5678-1234-567812345678")
//                .body("other-token")
//                .build();
//
//        Mockito.when(jwtProvider.verifySignature(Mockito.any(JWT.class))).thenReturn(true);
//        Mockito.when(repo.findById("12345678-1234-5678-1234-567812345678")).thenReturn(Optional.of(token));
//
//        Assertions.assertThrows(TokenMismatchException.class, () -> service.refreshToken(refreshToken));
//
//        Mockito.verifyNoInteractions(userRepo);
//    }
//
//    @Test
//    void testRefreshToken__TokenForNotExistingUser__ThrowUserNotFoundException() {
//        Mockito.doReturn(true).when(jwtProvider).verifySignature(Mockito.any(JWT.class));
//
//        RefreshToken token = RefreshToken.builder()
//                .id("12345678-1234-5678-1234-567812345678")
//                .body(refreshToken)
//                .build();
//
//        Mockito.when(repo.findById("12345678-1234-5678-1234-567812345678")).thenReturn(Optional.of(token));
//        Mockito.when(userRepo.findById(UUID.fromString("12345678-1234-5678-1234-567812345678")))
//                .thenReturn(Optional.empty());
//
//        Assertions.assertThrows(UserNotFoundException.class, () -> service.refreshToken(refreshToken));
//
//        Mockito.verify(jwtProvider).verifySignature(Mockito.any(JWT.class));
//        Mockito.verify(repo).findById(Mockito.anyString());
//        Mockito.verify(userRepo).findById(Mockito.any(UUID.class));
//        Mockito.verifyNoMoreInteractions(jwtProvider, repo, userRepo);
//    }
//}
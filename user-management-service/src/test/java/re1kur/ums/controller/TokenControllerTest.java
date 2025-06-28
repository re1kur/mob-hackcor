package re1kur.ums.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import re1kur.core.exception.TokenDidNotPassVerificationException;
import re1kur.core.exception.TokenMismatchException;
import re1kur.core.exception.TokenNotFoundException;
import re1kur.ums.controller.auth.TokenController;
import re1kur.core.dto.JwtToken;
import re1kur.ums.service.TokenService;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = TokenController.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc(addFilters = false)
class TokenControllerTest {
    @MockitoBean
    private TokenService service;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    private static final String URL = "/api/token";

    @Test
    void testRefreshToken__ValidToken__DoesNotThrowException() throws Exception {
        String refreshToken = "validTokenExample";
        JwtToken expected = Mockito.mock(JwtToken.class);

        Mockito.when(service.refreshToken("validTokenExample")).thenReturn(expected);

        mvc.perform(MockMvcRequestBuilders
                        .put(URL + "/refresh")
                        .content(refreshToken))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(expected)));
    }

    @Test
    void testRefreshToken__NotMatchedToken__ThrowsTokenMismatchException() throws Exception {
        String refreshToken = "tokenNotMatchWithReal";

        Mockito.when(service.refreshToken("tokenNotMatchWithReal")).thenThrow(TokenMismatchException.class);

        mvc.perform(MockMvcRequestBuilders
                        .put(URL + "/refresh")
                        .param("refreshToken", refreshToken))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testRefreshToken__InvalidToken__ThrowsInvalidTokenException() throws Exception {
        String refreshToken = "invalidTokenExample";

        Mockito.when(service.refreshToken("invalidTokenExample")).thenThrow(TokenDidNotPassVerificationException.class);

        mvc.perform(MockMvcRequestBuilders
                        .put(URL + "/refresh")
                        .param("refreshToken", refreshToken))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testRefreshToken__NotFoundToken__ThrowsTokenNotFoundException() throws Exception {
        String refreshToken = "tokenNotExist";

        Mockito.when(service.refreshToken("tokenNotExist")).thenThrow(TokenNotFoundException.class);

        mvc.perform(MockMvcRequestBuilders
                        .put(URL + "/refresh")
                        .param("refreshToken", refreshToken))
                .andExpect(status().isBadRequest());
    }
}
package re1kur.ums.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import re1kur.core.dto.UserDto;
import re1kur.core.exception.UserAlreadyRegisteredException;
import re1kur.core.exception.UserNotFoundException;
import re1kur.core.payload.LoginRequest;
import re1kur.core.payload.UserPayload;
import re1kur.ums.controller.auth.AuthenticationController;
import re1kur.ums.jwt.JwtToken;
import re1kur.ums.service.UserService;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AuthenticationController.class)
@ExtendWith(MockitoExtension.class)
public class AuthenticationControllerTest {
    private static final String URL = "/api/auth";

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper mapper;

    @MockitoBean
    UserService service;

    @Test
    void testRegister__ValidUser__DoesNotThrowException() throws Exception {
        UserPayload payload = UserPayload.builder()
                .email("email@example.com")
                .password("password")
                .firstname("firstname")
                .lastname("lastname")
                .build();
        UserDto expectedDto = UserDto.builder()
                .id("HERE-MUST-BE-UUID-ID")
                .email("email@example.com")
                .firstname("firstname")
                .lastname("lastname")
                .enabled(false)
                .build();

        Mockito.when(service.register(Mockito.any(UserPayload.class))).thenReturn(expectedDto);

        mvc.perform(MockMvcRequestBuilders
                        .post(URL + "/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(payload)))
                .andExpect(status().isCreated())
                .andExpect(content().json(mapper.writeValueAsString(expectedDto)));

        Mockito.verify(service, Mockito.times(1)).register(payload);
    }

    @Test
    void testRegister__AlreadyRegisteredUser__DoesThrowUserAlreadyRegisteredException() throws Exception {
        UserPayload payload = UserPayload.builder()
                .email("email@example.com")
                .password("password")
                .firstname("firstname")
                .lastname("lastname")
                .build();

        Mockito.when(service.register(Mockito.any(UserPayload.class))).thenThrow(UserAlreadyRegisteredException.class);

        mvc.perform(MockMvcRequestBuilders
                        .post(URL + "/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(payload)))
                .andExpect(status().isBadRequest());

        Mockito.verify(service, Mockito.times(1)).register(payload);
    }

    @Test
    void testRegister__UserWithInvalidEmail__ThrowMethodArgumentNotValidException() throws Exception {
        UserPayload invalidPayload = UserPayload.builder()
                .email("email")
                .password("password")
                .firstname("firstname")
                .lastname("lastname")
                .build();

        mvc.perform(MockMvcRequestBuilders
                        .post(URL + "/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(invalidPayload)))
                .andExpect(status().isBadRequest());

        Mockito.verifyNoInteractions(service);
    }

    @Test
    void testRegister__UserWithInvalidPassword__ThrowMethodArgumentNotValidException() throws Exception {
        UserPayload invalidPayload = UserPayload.builder()
                .email("email@example.com")
                .password("")
                .firstname("firstname")
                .lastname("lastname")
                .build();

        mvc.perform(MockMvcRequestBuilders
                        .post(URL + "/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(invalidPayload)))
                .andExpect(status().isBadRequest());

        Mockito.verifyNoInteractions(service);
    }

    @Test
    void testLogin__UserFound__DoesNotThrowException() throws Exception {
        LoginRequest request = LoginRequest.builder()
                .email("email@example.com")
                .password("password")
                .build();
        JwtToken expected = JwtToken.builder()
                .body("eyJaHeader.payload.signature")
                .build();

        Mockito.when(service.login(
                LoginRequest.builder()
                        .email("email@example.com")
                        .password("password")
                        .build())
        ).thenReturn(JwtToken.builder().body("eyJaHeader.payload.signature").build());

        mvc.perform(MockMvcRequestBuilders
                        .post(URL + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isFound())
                .andExpect(content().json(mapper.writeValueAsString(expected)));

        Mockito.verify(service, Mockito.times(1)).login(request);
    }

    @Test
    void testLogin__UserWithInvalidEmail__ThrowMethodArgumentNotValidException() throws Exception {
        LoginRequest request = LoginRequest.builder()
                .email("  ")
                .password("password")
                .build();

        mvc.perform(MockMvcRequestBuilders
                        .post(URL + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        Mockito.verifyNoInteractions(service);
    }

    @Test
    void testLogin__UserWithInvalidPassword__ThrowMethodArgumentNotValidException() throws Exception {
        LoginRequest request = LoginRequest.builder()
                .email("email@example.com")
                .password("    ")
                .build();

        mvc.perform(MockMvcRequestBuilders
                        .post(URL + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        Mockito.verifyNoInteractions(service);
    }

    @Test
    void testLogin__UserNotFound__ThrowUserNotFoundException() throws Exception {
        LoginRequest request = LoginRequest.builder()
                .email("email@example.com")
                .password("password")
                .build();

        Mockito.when(service.login(
                LoginRequest.builder()
                        .email("email@example.com")
                        .password("password")
                        .build()
        )).thenThrow(UserNotFoundException.class);

        mvc.perform(MockMvcRequestBuilders
                        .post(URL + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());

        Mockito.verify(service, Mockito.times(1)).login(request);
    }
}

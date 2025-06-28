package re1kur.ums.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import re1kur.core.exception.*;
import re1kur.core.payload.EmailVerificationPayload;
import re1kur.ums.service.VerificationService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = VerificationController.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc(addFilters = false)
public class VerificationControllerTest {
    private static final String URL = "/api/verify";

    @MockitoBean
    private VerificationService service;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @Test
    void testVerifyEmail__UserExists__ReturnsOk() throws Exception {
        EmailVerificationPayload payload = EmailVerificationPayload.builder()
                .email("email@example.com")
                .code("123456")
                .build();

        Mockito.doNothing().when(service).verifyEmail(payload);

        mvc.perform(put(URL + "/email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(payload)))
                .andExpect(status().isOk());

        Mockito.verify(service, Mockito.times(1)).verifyEmail(payload);
    }

    @Test
    void testVerifyEmail__InvalidEmailPayload__ReturnsBadRequest() throws Exception {
        EmailVerificationPayload payload = EmailVerificationPayload.builder()
                .email("email")
                .code("123456")
                .build();

        mvc.perform(put(URL + "/email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(payload)))
                .andExpect(status().isBadRequest());

        Mockito.verifyNoInteractions(service);
    }

    @Test
    void testVerifyEmail__InvalidCodePayload__ReturnsBadRequest() throws Exception {
        EmailVerificationPayload payload = EmailVerificationPayload.builder()
                .email("email@example.com")
                .code("231")
                .build();

        mvc.perform(put(URL + "/email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(payload)))
                .andExpect(status().isBadRequest());

        Mockito.verifyNoInteractions(service);
    }

    @Test
    void testVerifyEmail__UserNotExists__ReturnsBadRequest() throws Exception {
        EmailVerificationPayload payload = EmailVerificationPayload.builder()
                .email("email@example.com")
                .code("231123")
                .build();

        Mockito.doThrow(UserNotFoundException.class).when(service).verifyEmail(payload);

        mvc.perform(put(URL + "/email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(payload)))
                .andExpect(status().isBadRequest());

        Mockito.verify(service, Mockito.times(1)).verifyEmail(payload);
    }

    @Test
    void testVerifyEmail__CodeMismatch__ReturnsBadRequest() throws Exception {
        EmailVerificationPayload payload = EmailVerificationPayload.builder()
                .email("email@example.com")
                .code("231123")
                .build();

        Mockito.doThrow(CodeMismatchException.class).when(service).verifyEmail(payload);

        mvc.perform(put(URL + "/email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(payload)))
                .andExpect(status().isUnauthorized());

        Mockito.verify(service, Mockito.times(1)).verifyEmail(payload);

    }

    @Test
    void testVerifyEmail__CodeHasExpired__ReturnsGone() throws Exception {
        EmailVerificationPayload payload = EmailVerificationPayload.builder()
                .email("email@example.com")
                .code("231123")
                .build();

        Mockito.doThrow(CodeHasExpiredException.class).when(service).verifyEmail(payload);

        mvc.perform(put(URL + "/email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(payload)))
                .andExpect(status().isGone());

        Mockito.verify(service, Mockito.times(1)).verifyEmail(payload);
    }

    @Test
    void testVerifyEmail__CodeNotExists__ReturnsGone() throws Exception {
        EmailVerificationPayload payload = EmailVerificationPayload.builder()
                .email("email@example.com")
                .code("231123")
                .build();

        Mockito.doThrow(CodeNotFoundException.class).when(service).verifyEmail(payload);

        mvc.perform(put(URL + "/email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(payload)))
                .andExpect(status().isBadRequest());

        Mockito.verify(service, Mockito.times(1)).verifyEmail(payload);
    }

    @Test
    void testVerifyEmail__UserAlreadyVerified__ReturnsConflict() throws Exception {
        EmailVerificationPayload payload = EmailVerificationPayload.builder()
                .email("email@example.com")
                .code("231123")
                .build();

        Mockito.doThrow(UserAlreadyVerifiedException.class).when(service).verifyEmail(payload);

        mvc.perform(put(URL + "/email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(payload)))
                .andExpect(status().isConflict());

        Mockito.verify(service, Mockito.times(1)).verifyEmail(payload);
    }

    @Test
    void testGenerateCode__UserExists__ReturnsCreated() throws Exception {
        String email = "email@example.com";

        Mockito.doNothing().when(service).generateCode(email);

        mvc.perform(post(URL + "/code")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"email@example.com\"}"))
                .andExpect(status().isCreated());

        Mockito.verify(service, Mockito.times(1)).generateCode(email);
    }

    @Test
    void testGenerateCode__UserNotExists__ReturnsBadRequest() throws Exception {
        String email = "email@example.com";

        Mockito.doThrow(UserNotFoundException.class).when(service).generateCode(email);

        mvc.perform(post(URL + "/code")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"email@example.com\"}"))
                .andExpect(status().isBadRequest());

        Mockito.verify(service, Mockito.times(1)).generateCode(email);
    }
}

package re1kur.ums.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;

import re1kur.core.dto.UserDto;
import re1kur.core.dto.UserInformationDto;
import re1kur.core.exception.UserNotFoundException;
import re1kur.core.payload.UserInformationPayload;
import re1kur.ums.controller.info.UserInformationController;
import re1kur.ums.service.UserService;

import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = UserInformationController.class)
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class UserInformationControllerTest {
    @MockitoBean
    private UserService service;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    private static final String URL = "/api/users";

    private Jwt token;
    private UUID uuid;

    @BeforeEach
    void setUp() {
        uuid = UUID.randomUUID();
        token = Jwt
                .withTokenValue("eyJmock-token-value")
                .header("header", "value")
                .subject(uuid.toString()).build();
    }

    @Test
    void testGetRating__ReturnsList() throws Exception {
        UserDto user1 = Mockito.mock(UserDto.class);
        UserDto user2 = Mockito.mock(UserDto.class);
        UserDto user3 = Mockito.mock(UserDto.class);
        List<UserDto> expected = List.of(user1, user2, user3);

        Mockito.when(service.getUsersByRating()).thenReturn(List.of(user1, user2, user3));

        mvc.perform(MockMvcRequestBuilders
                        .get(URL + "/rating")
                        .with(jwt().jwt(token)))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(expected)));
    }

    @Test
    void testGetInfo__ReturnsInfoDto() throws Exception {
        UserDto user = UserDto.builder()
                .id(uuid.toString())
                .email("email")
                .enabled(true)
                .info(Mockito.mock(UserInformationDto.class))
                .build();

        Mockito.when(service.getPersonalInfo(Mockito.anyString())).thenReturn(UserDto.builder()
                .id(uuid.toString())
                .email("email")
                .enabled(true)
                .info(Mockito.mock(UserInformationDto.class))
                .build());

        mvc.perform(MockMvcRequestBuilders
                        .get(URL + "/info")
                        .with(jwt()
                                .jwt(token)))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(user)));
    }

    @Test
    void testGetInfo__UserNotFound__ReturnsBadRequest() throws Exception {
        Mockito.when(service.getPersonalInfo(Mockito.anyString())).thenThrow(UserNotFoundException.class);

        mvc.perform(MockMvcRequestBuilders
                        .get(URL + "/info")
                        .with(jwt().jwt(token)))
                .andExpect(status().isBadRequest());

        Mockito.verify(service, Mockito.times(1)).getPersonalInfo(Mockito.anyString());
    }

    @Test
    void testUpdateInfo__ReturnsUpdatedInfoDto() throws Exception {
        UserInformationPayload payload = new UserInformationPayload("firstnameUpdated", "lastnameUpdated");
        UserInformationDto updated = UserInformationDto.builder().firstname("firstnameUpdated").lastname("lastnameUpdated").build();
        UserDto updatedUser = UserDto.builder()
                .id(uuid.toString())
                .email("email")
                .enabled(true)
                .info(updated)
                .build();

        Mockito.when(service.updateUserInfo(payload, uuid.toString())).thenReturn(UserDto.builder()
                .id(uuid.toString())
                .email("email")
                .enabled(true)
                .info(UserInformationDto.builder().firstname("firstnameUpdated").lastname("lastnameUpdated").build())
                .build());

        mvc.perform(MockMvcRequestBuilders
                        .put(URL + "/info/update")
                        .with(jwt()
                                .jwt(token)
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(payload)))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(updatedUser)));
    }

    @Test
    void testUpdateInfo__UserNotFound__ReturnsBadRequest() throws Exception {
        UserInformationPayload payload = new UserInformationPayload("firstnameUpdated", "lastnameUpdated");


        Mockito.when(service.updateUserInfo(payload, uuid.toString())).thenThrow(UserNotFoundException.class);

        mvc.perform(MockMvcRequestBuilders
                        .put(URL + "/info/update")
                        .with(jwt()
                                .jwt(token)
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(payload)))
                .andExpect(status().isBadRequest());
    }
}
package re1kur.ums.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import re1kur.core.dto.UserDto;
import re1kur.core.dto.UserInformationDto;
import re1kur.core.exception.UserNotFoundException;
import re1kur.core.payload.UserInformationPayload;
import re1kur.ums.entity.User;
import re1kur.ums.entity.UserInformation;
import re1kur.ums.mapper.UserMapper;
import re1kur.ums.repository.sql.UserInformationRepository;
import re1kur.ums.repository.sql.UserRepository;
import re1kur.ums.service.impl.UserServiceImpl;

import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @InjectMocks
    private UserServiceImpl service;

    @Mock
    private UserRepository userRepo;

    @Mock
    private UserInformationRepository infoRepo;

    @Mock
    private UserMapper mapper;

    private UUID id;

    private User user;

    private UserDto userDto;

    @BeforeEach
    void setUp() {
        id = UUID.randomUUID();
        UserInformation info = UserInformation.builder()
                .userId(id)
                .firstname("firstname")
                .lastname("lastname")
                .build();
        user = User.builder()
                .id(id)
                .information(info)
                .build();

        UserInformationDto infoDto = UserInformationDto.builder()
                .firstname("firstname")
                .lastname("lastname")
                .build();
        userDto = UserDto.builder()
                .id(id.toString())
                .info(infoDto)
                .build();
    }

    @Test
    void testGetPersonalInfo__ReturnsUserDto() {
        Mockito.when(userRepo.findById(id)).thenReturn(Optional.of(user));
        Mockito.when(mapper.read(user)).thenReturn(userDto);

        UserDto result = Assertions.assertDoesNotThrow(() -> service.getPersonalInfo(id.toString()));
        Assertions.assertEquals(userDto, result);

        Mockito.verify(userRepo, Mockito.times(1)).findById(id);
        Mockito.verify(mapper, Mockito.times(1)).read(user);
    }

    @Test
    void testGetPersonalInfo__UserNotExists__ThrowsUserNotFoundException() {
        Mockito.when(userRepo.findById(id)).thenReturn(Optional.empty());

        Assertions.assertThrows(UserNotFoundException.class, () -> service.getPersonalInfo(id.toString()));

        Mockito.verify(userRepo, Mockito.times(1)).findById(id);
        Mockito.verifyNoInteractions(mapper);
    }

    @Test
    void testUpdatePersonalInfo__ReturnsUserDto() {
        UserInformationPayload payload = new UserInformationPayload("firstnameUpdated", "lastnameUpdated");
        UserInformation info = UserInformation.builder()
                .userId(id)
                .firstname("firstnameUpdated")
                .lastname("lastnameUpdated")
                .build();
        User updated = User.builder()
                .id(id)
                .information(info)
                .build();
        UserInformationDto infoDto = UserInformationDto.builder()
                .firstname("firstnameUpdated")
                .lastname("lastnameUpdated")
                .build();
        UserDto expected = UserDto.builder()
                .id(id.toString())
                .info(infoDto)
                .build();

        Mockito.when(userRepo.findById(id)).thenReturn(Optional.of(user));
        Mockito.when(mapper.read(updated)).thenReturn(expected);

        UserDto result = Assertions.assertDoesNotThrow(() -> service.updateUserInfo(payload, id.toString()));
        Assertions.assertEquals(expected, result);

        Mockito.verify(userRepo, Mockito.times(1)).findById(id);
        Mockito.verify(mapper, Mockito.times(1)).read(updated);
    }

    @Test
    void testUpdatePersonalInfo__UserNotExists__ThrowsUserNotFoundException() {
        UserInformationPayload payload = new UserInformationPayload("firstnameUpdated", "lastnameUpdated");

        Mockito.when(userRepo.findById(id)).thenReturn(Optional.empty());

        Assertions.assertThrows(UserNotFoundException.class, () -> service.updateUserInfo(payload, id.toString()));

        Mockito.verify(userRepo, Mockito.times(1)).findById(id);
        Mockito.verifyNoInteractions(infoRepo, mapper);
    }


}
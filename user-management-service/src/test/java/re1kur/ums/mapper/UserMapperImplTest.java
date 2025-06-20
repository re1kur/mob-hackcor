package re1kur.ums.mapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import re1kur.core.dto.UserDto;
import re1kur.core.payload.UserPayload;
import re1kur.ums.entity.User;
import re1kur.ums.mapper.impl.UserMapperImpl;

import java.util.UUID;


@ExtendWith(MockitoExtension.class)
class UserMapperImplTest {
    @InjectMocks
    private UserMapperImpl mapper;

    @Mock
    private PasswordEncoder encoder;

    @Test
    void testWrite_EncodesPasswordAndBuildsUser() {
        UserPayload payload = Mockito.mock(UserPayload.class);
        Mockito.when(payload.email()).thenReturn("test@example.com");
        Mockito.when(payload.password()).thenReturn("plainPassword");
        Mockito.when(encoder.encode("plainPassword")).thenReturn("encodedPassword");

        User user = mapper.write(payload);

        Assertions.assertNotNull(user);
        Assertions.assertEquals("test@example.com", user.getEmail());
        Assertions.assertEquals("encodedPassword", user.getPassword());

        Mockito.verify(payload).email();
        Mockito.verify(payload).password();
        Mockito.verify(encoder).encode("plainPassword");
    }

    @Test
    void testRead_MapsUserToUserDto() {
        User user = User.builder()
                .id(UUID.randomUUID())
                .email("user@example.com")
                .enabled(true)
                .build();

        UserDto dto = mapper.read(user);

        Assertions.assertNotNull(dto);
        Assertions.assertEquals(user.getId().toString(), dto.id());
        Assertions.assertEquals(user.getEmail(), dto.email());
        Assertions.assertEquals(user.getEnabled(), dto.enabled());
    }
}
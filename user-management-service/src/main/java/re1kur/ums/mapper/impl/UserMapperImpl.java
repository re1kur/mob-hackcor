package re1kur.ums.mapper.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import re1kur.core.dto.UserDto;
import re1kur.core.dto.UserInformationDto;
import re1kur.core.payload.UserPayload;
import re1kur.ums.entity.User;
import re1kur.ums.entity.UserInformation;
import re1kur.ums.mapper.UserMapper;

@Component
@RequiredArgsConstructor
public class UserMapperImpl implements UserMapper {
    private final PasswordEncoder encoder;

    @Override
    public User write(UserPayload payload) {
        return User.builder()
                .email(payload.email())
                .password(encoder.encode(payload.password()))
                .build();
    }

    @Override
    public UserDto read(User user) {
        UserInformationDto userInfoDto = null;
        if (user.getInformation() != null) {
            UserInformation info = user.getInformation();
            userInfoDto = UserInformationDto.builder()
                    .firstname(info.getFirstname())
                    .lastname(info.getLastname())
                    .level(info.getLevel())
                    .rating(info.getRating())
                    .build();
        }

        return UserDto.builder()
                .id(user.getId().toString())
                .email(user.getEmail())
                .enabled(user.getEnabled())
                .info(userInfoDto)
                .build();
    }
}

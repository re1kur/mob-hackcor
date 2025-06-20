package re1kur.ums.mapper.impl;

import org.springframework.stereotype.Component;
import re1kur.core.dto.UserDto;
import re1kur.core.payload.UserPayload;
import re1kur.ums.entity.User;
import re1kur.ums.mapper.UserMapper;

@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public User write(UserPayload payload) {
        return null;
    }

    @Override
    public UserDto read(User payload) {
        return null;
    }
}

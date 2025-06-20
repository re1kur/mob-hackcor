package re1kur.ums.mapper;

import re1kur.core.dto.UserDto;
import re1kur.core.payload.UserPayload;
import re1kur.ums.entity.User;

public interface UserMapper {
    User write(UserPayload payload);

    UserDto read(User payload);
}

package re1kur.ums.service;

import re1kur.core.dto.UserDto;
import re1kur.ums.entity.UserInformation;

import java.util.List;

public interface UserService {
    void reward(String userId, Integer reward);

    List<UserDto> getUsersByRating(Integer size);

    UserDto getPersonalInfo(String sub);
}

package re1kur.ums.service;

import re1kur.core.dto.UserDto;
import re1kur.core.payload.UserInformationPayload;

import java.util.List;

public interface UserService {
    void reward(String userId, Integer reward);

    List<UserDto> getUsersByRating();

    UserDto getPersonalInfo(String sub);

    UserDto updateUserInfo(UserInformationPayload payload, String subject);
}

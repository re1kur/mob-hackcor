package re1kur.ums.service;

import re1kur.core.dto.UserDto;

import java.util.List;

public interface UserService {
    void reward(String userId, Integer reward);

    List<UserDto> getUsersByRating(Integer size);
}

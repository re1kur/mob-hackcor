package re1kur.ums.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import re1kur.core.dto.UserDto;
import re1kur.ums.entity.UserInformation;
import re1kur.ums.mapper.UserMapper;
import re1kur.ums.repository.sql.UserInformationRepository;
import re1kur.ums.repository.sql.UserRepository;
import re1kur.ums.service.UserService;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserInformationRepository infoRepo;
    private final UserRepository userRepo;
    private final UserMapper mapper;

    @Override
    public void reward(String userId, Integer reward) {
        UserInformation information = infoRepo.findByUserId(UUID.fromString(userId)).orElseThrow();
        information.setRating(information.getRating() + reward);
        infoRepo.save(information);
    }

    @Override
    public List<UserDto> getUsersByRating(Integer size) {
        return userRepo.findAllOrderByRating(size)
                .stream().map(mapper::read)
                .toList();
    }

    @Override
    public UserDto getPersonalInfo(String sub) {
        return mapper.read(userRepo.getReferenceById(UUID.fromString(sub)));
    }
}

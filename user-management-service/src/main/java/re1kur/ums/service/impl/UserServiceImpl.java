package re1kur.ums.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import re1kur.core.dto.UserDto;
import re1kur.core.exception.UserNotFoundException;
import re1kur.core.payload.UserInformationPayload;
import re1kur.ums.entity.User;
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
    @Transactional(readOnly = true)
    public UserDto getPersonalInfo(String sub) {
        return mapper.read(userRepo.findById(
                UUID.fromString(sub)).orElseThrow(() ->
                new UserNotFoundException("User %s not found.".formatted(sub))));
    }

    @Override
    public UserDto updateUserInfo(UserInformationPayload payload, String subject) {
        User user = userRepo.findById(UUID.fromString(subject)).orElseThrow(() -> new UserNotFoundException("User %s not found.".formatted(subject)));
        UserInformation information = user.getInformation();
        if (information == null) {
            information = UserInformation.builder()
                    .userId(user.getId())
                    .build();
            user.setInformation(information);
        }
        information.setFirstname(payload.firstname());
        information.setLastname(payload.lastname());
        infoRepo.save(information);
        return mapper.read(user);
    }
}

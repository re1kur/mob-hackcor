package re1kur.ums.controller.info;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import re1kur.core.dto.UserDto;
import re1kur.ums.service.UserService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserInformationController {
    private final UserService service;

    @GetMapping("/rating")
    public ResponseEntity<List<UserDto>> getRating(@RequestParam Integer size) {
        return ResponseEntity.status(HttpStatus.OK).body(service.getUsersByRating(size));
    }

    @GetMapping("/info")
    public ResponseEntity<UserDto> getInfo(@AuthenticationPrincipal Jwt token) {
        return ResponseEntity.status(HttpStatus.OK).body(service.getPersonalInfo(token.getSubject()));
    }
}

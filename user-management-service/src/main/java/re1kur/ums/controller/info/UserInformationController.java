package re1kur.ums.controller.info;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import re1kur.core.dto.UserDto;
import re1kur.core.payload.UserInformationPayload;
import re1kur.ums.service.UserService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserInformationController {
    private final UserService service;

    @GetMapping("/rating")
    public ResponseEntity<List<UserDto>> getRating() {
        return ResponseEntity.status(HttpStatus.OK).body(service.getUsersByRating());
    }

    @GetMapping("/info")
    public ResponseEntity<UserDto> getInfo(@AuthenticationPrincipal Jwt token) {
        return ResponseEntity.status(HttpStatus.OK).body(service.getPersonalInfo(token.getSubject()));
    }

    @PutMapping("/info/update")
    public ResponseEntity<UserDto> updateInfo(@AuthenticationPrincipal Jwt token,
                                              @RequestBody UserInformationPayload payload) {
        UserDto body = service.updateUserInfo(payload, token.getSubject());
        return ResponseEntity.ok(body);
    }
}

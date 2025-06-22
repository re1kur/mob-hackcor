package re1kur.ums.controller.info;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import re1kur.core.dto.UserDto;
import re1kur.ums.service.UserService;

import java.util.List;

@RestController("/api/users")
@RequiredArgsConstructor
public class UserInformationController {
    private final UserService service;

    @GetMapping("/rating")
    public ResponseEntity<List<UserDto>> getRating(@RequestParam Integer size) {
        return ResponseEntity.ok(service.getUsersByRating(size));
    }

    @GetMapping("/info")
    public ResponseEntity<UserDto> getInfo(@AuthenticationPrincipal JwtAuthenticationToken token) {
        return ResponseEntity.status(HttpStatus.FOUND).body(service.getPersonalInfo(token.getTokenAttributes().get("sub").toString()));
    }
}

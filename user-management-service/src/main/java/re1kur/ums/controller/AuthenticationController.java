package re1kur.ums.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import re1kur.core.payload.LoginRequest;
import re1kur.core.payload.UserPayload;
import re1kur.core.dto.JwtToken;
import re1kur.ums.service.AuthService;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthenticationController {
    private final AuthService service;

    @PostMapping("/register")
    public ResponseEntity<UUID> register(@RequestBody @Valid UserPayload payload) {
        UUID body = service.register(payload);
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtToken> login(@RequestBody @Valid LoginRequest request) {
        JwtToken body = service.login(request);
        return ResponseEntity.status(HttpStatus.OK).body(body);
    }
}

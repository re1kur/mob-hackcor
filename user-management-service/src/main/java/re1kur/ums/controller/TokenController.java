package re1kur.ums.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import re1kur.ums.jwt.JwtToken;
import re1kur.ums.service.TokenService;

import java.text.ParseException;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/token")
public class TokenController {
    private final TokenService service;

    @PutMapping("refresh")
    public ResponseEntity<JwtToken> refresh(@RequestParam String refreshToken) throws ParseException {
        JwtToken token = service.refreshToken(refreshToken);
        return ResponseEntity.ok(token);
    }
}

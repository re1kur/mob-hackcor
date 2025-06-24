package re1kur.ums.controller.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import re1kur.core.dto.JwtToken;
import re1kur.ums.service.TokenService;

import java.text.ParseException;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/token")
public class TokenController {
    private final TokenService service;

    @PutMapping("refresh")
    public ResponseEntity<JwtToken> refresh(@RequestBody String refreshToken) throws ParseException {
        JwtToken token = service.refreshToken(refreshToken);
        return ResponseEntity.ok(token);
    }

    @GetMapping("jwks")
    public Map<String, Object> getJwks() throws Exception {
        return service.getPublicKey();
    }
}

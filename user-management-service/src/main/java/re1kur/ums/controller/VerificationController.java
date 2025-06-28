package re1kur.ums.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import re1kur.core.payload.EmailPayload;
import re1kur.core.payload.EmailVerificationPayload;
import re1kur.ums.service.VerificationService;

@RestController
@RequestMapping("/api/verify")
@RequiredArgsConstructor
public class VerificationController {
    private final VerificationService service;

    @PutMapping("/email")
    public ResponseEntity<Void> verifyEmail(@RequestBody @Valid EmailVerificationPayload payload) {
        service.verifyEmail(payload);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/code")
    public ResponseEntity<Void> generateCode(@RequestBody @Valid EmailPayload payload) {
        service.generateCode(payload.email());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}

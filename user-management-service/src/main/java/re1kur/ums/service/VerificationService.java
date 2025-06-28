package re1kur.ums.service;

import re1kur.core.payload.EmailVerificationPayload;

public interface VerificationService {
    void verifyEmail(EmailVerificationPayload payload);

    void generateCode(String email);
}

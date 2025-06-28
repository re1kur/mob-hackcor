package re1kur.ums.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import re1kur.core.exception.*;
import re1kur.core.other.CodeGenerator;
import re1kur.core.payload.EmailVerificationPayload;
import re1kur.ums.entity.Code;
import re1kur.ums.entity.User;
import re1kur.ums.mapper.CodeMapper;
import re1kur.ums.repository.redis.CodeRepository;
import re1kur.ums.repository.sql.UserRepository;
import re1kur.ums.service.VerificationService;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class VerificationServiceImpl implements VerificationService {
    private final UserRepository userRepo;
    private final CodeRepository codeRepo;
    private final CodeMapper mapper;

    @Override
    public void verifyEmail(EmailVerificationPayload payload) {
        String email = payload.email();
        User user = userRepo.findByEmail(email).orElseThrow(
                () -> new UserNotFoundException("User with email %s not found.".formatted(email)));
        if (user.getEnabled())
            throw new UserAlreadyVerifiedException("User with email %s already verified.".formatted(email));
        String id = user.getId().toString();
        Optional<Code> maybeCode = codeRepo.findById(id);
        validateCode(maybeCode, id, payload.code());

        user.setEnabled(true);
        userRepo.save(user);
    }

    @Override
    public void generateCode(String email) {
        User user = userRepo.findByEmail(email).orElseThrow(
                () -> new UserNotFoundException("User with email %s not found.".formatted(email)));
        String value = CodeGenerator.generateOTP();
        Code mapped = mapper.write(user.getId().toString(), value);

        codeRepo.save(mapped);
        log.info("Generated code '{}' for user '{}'.", value, email);
    }

    private void validateCode(Optional<Code> maybeCode, String id, String expected) {
        if (maybeCode.isEmpty()) {
            throw new CodeNotFoundException("Code for user %s was not found.".formatted(id));
        }
        Code code = maybeCode.get();
        if (code.isExpired()) {
            throw new CodeHasExpiredException("Code has expired. Generated new code and sent");
        }
        if (!code.isMatches(expected)) {
            throw new CodeMismatchException("Code mismatched with payload code %s.".formatted(expected));
        }
    }
}

package re1kur.core.payload;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record EmailVerificationPayload(
        @Email(message = "The email is incorrect.")
        @Size(max = 256)
        String email,
        @NotBlank(message = "The code can not be empty or contain backspaces.")
        @Size(min = 6, max = 6, message = "The code is only 6 symbols long.")
        String code
) {
}

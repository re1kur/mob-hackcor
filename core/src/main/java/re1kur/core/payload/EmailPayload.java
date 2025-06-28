package re1kur.core.payload;

import jakarta.validation.constraints.Email;

public record EmailPayload(
        @Email(message = "The email is incorrect.")
        String email
) {
}

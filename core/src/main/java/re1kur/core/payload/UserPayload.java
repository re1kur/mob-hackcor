package re1kur.core.payload;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record UserPayload(
        @Email(message = "The email is invalid.")
        String email,
        @Size(min = 6, max = 256, message = "The password must be between 6 and 256 characters long.")
        @NotBlank(message = "Password can be empty or contain backspace.")
        String password) {
}

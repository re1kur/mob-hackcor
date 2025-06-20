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
        String password,
        @NotBlank(message = "Firstname can be empty or contain backspace.")
        @Size(min = 3, max = 64, message = "The firstname must be between 3 and 64 characters long.")
        String firstname,
        @NotBlank(message = "Lastname can be empty or contain backspace.")
        @Size(max = 64, message = "The lastname must be greater 64 characters long.")
        String lastname) {
}

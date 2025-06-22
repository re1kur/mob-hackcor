package re1kur.core.dto;

import lombok.Builder;

@Builder
public record UserInformationDto(
        String firstname,
        String lastname,
        Integer rating,
        String level
) {
}

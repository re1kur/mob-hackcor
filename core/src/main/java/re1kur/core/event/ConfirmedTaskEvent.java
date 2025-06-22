package re1kur.core.event;

import lombok.Builder;

@Builder
public record ConfirmedTaskEvent (
        String userId,
        Integer reward
) {
}

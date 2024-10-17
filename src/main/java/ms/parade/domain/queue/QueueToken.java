package ms.parade.domain.queue;

import java.time.LocalDateTime;

public record QueueToken(
    long uuid,
    long userId,
    QueueTokenStatus status,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
}

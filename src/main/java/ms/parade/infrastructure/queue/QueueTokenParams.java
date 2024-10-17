package ms.parade.infrastructure.queue;

import java.time.LocalDateTime;

import ms.parade.domain.queue.QueueTokenStatus;

public record QueueTokenParams(long userId, LocalDateTime createdAt, LocalDateTime updatedAt, QueueTokenStatus status) {
}

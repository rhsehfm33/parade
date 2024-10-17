package ms.parade.domain.queue;

public record QueueTokenInfo(
    QueueToken queueToken,
    int order
) {
}

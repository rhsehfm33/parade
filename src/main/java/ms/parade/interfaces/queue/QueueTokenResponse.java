package ms.parade.interfaces.queue;

import ms.parade.domain.queue.QueueTokenInfo;

public record QueueTokenResponse(
    QueueTokenInfo queueTokenInfo
) {
}

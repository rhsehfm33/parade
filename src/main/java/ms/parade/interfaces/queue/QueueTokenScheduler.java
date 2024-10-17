package ms.parade.interfaces.queue;

import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import ms.parade.domain.queue.QueueTokenInfo;
import ms.parade.domain.queue.QueueTokenService;
import ms.parade.domain.queue.QueueTokenStatus;

@Component
@RequiredArgsConstructor
public class QueueTokenScheduler {
    private final QueueTokenService queueTokenService;

    @Transactional
    @Scheduled(fixedRate = 60000)
    public void passQueueTokens() {
        List<QueueTokenInfo> queueTokenInfos = queueTokenService.findFrontWaits();
        for (QueueTokenInfo queueTokenInfo : queueTokenInfos) {
            queueTokenService.update(queueTokenInfo.queueToken().uuid(), QueueTokenStatus.PASS);
        }
    }

    @Transactional
    @Scheduled(fixedRate = 60000)
    public void deleteQueueTokens() {
        List<QueueTokenInfo> queueTokenInfos = queueTokenService.findTimeoutPasses();
        for (QueueTokenInfo queueTokenInfo : queueTokenInfos) {
            queueTokenService.deleteById(queueTokenInfo.queueToken().uuid());
        }
    }

}

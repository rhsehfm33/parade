package ms.parade.domain.queue;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import ms.parade.infrastructure.queue.QueueTokenParams;

@Service
@Transactional
@RequiredArgsConstructor
public class QueueTokenService {
    private final QueueTokenRepository queueTokenRepository;

    public QueueTokenInfo putUnique(long userId) {
        final Optional<QueueToken> queueTokenOptional = queueTokenRepository.findByUserId(userId);
        if (queueTokenOptional.isPresent()) {
            throw new IllegalArgumentException("Token already exists");
        }

        QueueTokenParams queueTokenParams = new QueueTokenParams(
            userId,
            LocalDateTime.now(),
            null,
            QueueTokenStatus.WAIT
        );
        QueueToken queueToken = queueTokenRepository.save(queueTokenParams);

        int order = queueTokenRepository.getWaitOrderByTime(queueToken.createdAt());
        return new QueueTokenInfo(queueToken, order + 1);
    }

    public QueueTokenInfo getById(long uuid) {
        QueueToken queueToken = queueTokenRepository.findById(uuid).orElseThrow(
            () -> new IllegalArgumentException("UUID_NOT_EXIST; No token found for uuid: " + uuid)
        );
        int order = queueTokenRepository.getWaitOrderByTime(queueToken.createdAt());
        return new QueueTokenInfo(queueToken, order + 1);
    }

    public List<QueueTokenInfo> findFrontWaits() {
        AtomicInteger index = new AtomicInteger(1);
        return queueTokenRepository.findFrontWaits().stream()
            .map(queueToken -> new QueueTokenInfo(queueToken, index.getAndIncrement()))
            .toList();
    }

    public List<QueueTokenInfo> findTimeoutPasses() {
        return queueTokenRepository.findTimeoutWaits().stream()
            .map(queueToken -> new QueueTokenInfo(queueToken, -1))
            .toList();
    }

    public QueueTokenInfo update(long uuid, QueueTokenStatus queueTokenStatus) {
        QueueToken queueToken = queueTokenRepository.updateStatus(uuid, queueTokenStatus);
        return new QueueTokenInfo(queueToken, 0);
    }

    public void deleteById(long id) {
        queueTokenRepository.deleteById(id);
    }

    public void passQueueTokens() {
        queueTokenRepository.findFrontWaits().forEach(
            queueToken -> update(queueToken.uuid(), QueueTokenStatus.PASS)
        );
    }

    public void deleteQueueTokens() {
        queueTokenRepository.findTimeoutWaits().forEach(queueToken -> deleteById(queueToken.uuid()));
    }
}

package ms.parade.infrastructure.queue;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import ms.parade.domain.queue.QueueToken;
import ms.parade.domain.queue.QueueTokenRepository;
import ms.parade.domain.queue.QueueTokenStatus;

@Repository
@RequiredArgsConstructor
public class QueueTokenRepositoryImpl implements QueueTokenRepository {
    private final QueueTokenJpaRepository queueTokenJpaRepository;

    @Override
    public QueueToken save(QueueTokenParams queueTokenParams) {
        QueueTokenEntity queueTokenEntity = QueueTokenEntity.from(queueTokenParams);
        queueTokenEntity = queueTokenJpaRepository.save(queueTokenEntity);
        return QueueTokenEntity.to(queueTokenEntity);
    }

    @Override
    public QueueToken updateStatus(long id, QueueTokenStatus queueTokenStatus) {
        QueueTokenEntity queueTokenEntity = queueTokenJpaRepository.findById(id).orElseThrow(
            () -> new IllegalArgumentException("존재하지 않는 토큰입니다.")
        );
        queueTokenEntity.setStatus(queueTokenStatus);
        queueTokenEntity.setUpdatedAt(LocalDateTime.now());
        queueTokenJpaRepository.save(queueTokenEntity);
        return QueueTokenEntity.to(queueTokenEntity);
    }

    @Override
    public int getWaitOrderByTime(LocalDateTime time) {
        return queueTokenJpaRepository.countByStatusAndCreatedAtBefore(QueueTokenStatus.WAIT, time);
    }

    @Override
    public Optional<QueueToken> findById(long id) {
        Optional<QueueTokenEntity> queueTokenEntity = queueTokenJpaRepository.findById(id);
        return queueTokenEntity.map(QueueTokenEntity::to);
    }

    @Override
    public Optional<QueueToken> findByUserId(long userId) {
        Optional<QueueTokenEntity> queueTokenEntity = queueTokenJpaRepository.findByUserId(userId);
        return queueTokenEntity.map(QueueTokenEntity::to);
    }

    @Override
    public void deleteById(long id) {
        queueTokenJpaRepository.deleteById(id);
    }

    @Override
    public List<QueueToken> findFrontWaits() {
        return queueTokenJpaRepository.findTop30ByStatusOrderByIdAsc(QueueTokenStatus.WAIT).stream()
            .map(QueueTokenEntity::to)
            .toList();
    }

    @Override
    public List<QueueToken> findTimeoutWaits() {
        return queueTokenJpaRepository.findByStatusAndUpdatedAtBefore(
            QueueTokenStatus.PASS, LocalDateTime.now().minusMinutes(20)
        ).stream().map(QueueTokenEntity::to).toList();
    }
}

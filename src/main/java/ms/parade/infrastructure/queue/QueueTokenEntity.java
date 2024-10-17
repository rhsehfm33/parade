package ms.parade.infrastructure.queue;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ms.parade.domain.queue.QueueToken;
import ms.parade.domain.queue.QueueTokenStatus;

@Entity
@Getter
@Table(name = "queue_tokens")
@NoArgsConstructor
public class QueueTokenEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private long userId;

    @Setter
    @Enumerated(EnumType.STRING)
    private QueueTokenStatus status;

    private LocalDateTime createdAt;

    @Setter
    private LocalDateTime updatedAt;

    public static QueueTokenEntity from(QueueTokenParams queueTokenParams) {
        QueueTokenEntity tokenEntity = new QueueTokenEntity();
        tokenEntity.userId = queueTokenParams.userId();
        tokenEntity.status = queueTokenParams.status();
        tokenEntity.createdAt = queueTokenParams.createdAt();
        tokenEntity.updatedAt = queueTokenParams.updatedAt();
        return tokenEntity;
    }

    public static QueueToken to(QueueTokenEntity queueTokenEntity) {
        return new QueueToken(
            queueTokenEntity.id,
            queueTokenEntity.userId,
            queueTokenEntity.status,
            queueTokenEntity.createdAt,
            queueTokenEntity.updatedAt
        );
    }
}

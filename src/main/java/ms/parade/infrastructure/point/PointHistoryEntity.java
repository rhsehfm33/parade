package ms.parade.infrastructure.point;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ms.parade.domain.point.PointHistory;
import ms.parade.domain.point.PointType;

@Entity
@Getter
@Table(name = "point_history")
@NoArgsConstructor
public class PointHistoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private long userId;

    @Enumerated(EnumType.STRING)
    private PointType type;

    private long amount;

    private String reason;

    public static PointHistoryEntity from(PointHistoryParams pointHistoryParams) {
        PointHistoryEntity pointHistoryEntity = new PointHistoryEntity();
        pointHistoryEntity.userId = pointHistoryParams.userId();
        pointHistoryEntity.type = pointHistoryParams.type();
        pointHistoryEntity.amount = pointHistoryParams.amount();
        pointHistoryEntity.reason = pointHistoryParams.reason();
        return pointHistoryEntity;
    }

    public static PointHistory to(PointHistoryEntity pointHistoryEntity) {
        return new PointHistory(
            pointHistoryEntity.id,
            pointHistoryEntity.userId,
            pointHistoryEntity.type,
            pointHistoryEntity.amount,
            pointHistoryEntity.reason
        );
    }
}

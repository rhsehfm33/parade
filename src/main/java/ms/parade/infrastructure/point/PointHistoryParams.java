package ms.parade.infrastructure.point;

import ms.parade.domain.point.PointType;

public record PointHistoryParams(
    long userId,
    PointType type,
    long amount,
    String reason
) {
}

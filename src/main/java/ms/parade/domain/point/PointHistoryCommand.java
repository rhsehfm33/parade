package ms.parade.domain.point;

import ms.parade.infrastructure.point.PointHistoryParams;

public record PointHistoryCommand(
    PointHistoryParams pointHistoryParams
) {
}

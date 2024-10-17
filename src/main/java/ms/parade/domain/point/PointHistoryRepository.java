package ms.parade.domain.point;

import ms.parade.infrastructure.point.PointHistoryParams;

public interface PointHistoryRepository {
    PointHistory save(PointHistoryParams pointHistoryParams);
}

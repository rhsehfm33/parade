package ms.parade.infrastructure.point;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import ms.parade.domain.point.PointHistory;
import ms.parade.domain.point.PointHistoryRepository;

@Repository
@RequiredArgsConstructor
public class PointHistoryRepositoryImpl implements PointHistoryRepository {
    private final PointHistoryJpaRepository pointHistoryJpaRepository;

    @Override
    public PointHistory save(PointHistoryParams pointHistoryParams) {
        PointHistoryEntity pointHistoryEntity = PointHistoryEntity.from(pointHistoryParams);
        pointHistoryEntity = pointHistoryJpaRepository.save(pointHistoryEntity);
        return PointHistoryEntity.to(pointHistoryEntity);
    }
}

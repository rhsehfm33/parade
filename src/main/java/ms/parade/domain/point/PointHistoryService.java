package ms.parade.domain.point;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class PointHistoryService {
    private final PointHistoryRepository pointHistoryRepository;

    public PointHistory record(PointHistoryCommand pointHistoryCommand) {
        return pointHistoryRepository.save(pointHistoryCommand.pointHistoryParams());
    }
}

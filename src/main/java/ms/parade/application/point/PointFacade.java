package ms.parade.application.point;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import ms.parade.domain.point.PointHistory;
import ms.parade.domain.point.PointHistoryCommand;
import ms.parade.domain.point.PointHistoryService;
import ms.parade.domain.point.PointType;
import ms.parade.domain.user.User;
import ms.parade.domain.user.UserService;
import ms.parade.infrastructure.point.PointHistoryParams;

@Service
@Transactional
@RequiredArgsConstructor
public class PointFacade {
    private final UserService userService;
    private final PointHistoryService pointHistoryService;

    public UserPointResult chargePoint(long userId, long amount) {
        User user = userService.updatePoint(userId, amount, PointType.CHARGE);

        PointHistoryParams pointHistoryParams = new PointHistoryParams(
            userId,
            PointType.CHARGE,
            amount,
            "포인트 충전"
        );
        PointHistoryCommand pointHistoryCommand = new PointHistoryCommand(pointHistoryParams);
        PointHistory pointHistory = pointHistoryService.record(pointHistoryCommand);

        return new UserPointResult(user, pointHistory);
    }

    public long getUserPoint(long userId) {
        User user = userService.findById(userId).orElseThrow(
            () -> new IllegalArgumentException("존재하지 않는 사용자입니다.")
        );
        return user.point();
    }
}

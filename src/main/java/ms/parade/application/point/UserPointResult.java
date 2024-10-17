package ms.parade.application.point;

import ms.parade.domain.point.PointHistory;
import ms.parade.domain.user.User;

public record UserPointResult(
    User user,
    PointHistory pointHistory
) {
}

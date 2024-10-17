package ms.parade.infrastructure.seat;

import ms.parade.domain.seat.SeatStatus;

public record SeatParams(
    long scheduleId,
    String name,
    long price,
    SeatStatus status
) {
}

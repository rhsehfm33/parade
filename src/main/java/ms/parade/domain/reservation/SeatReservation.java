package ms.parade.domain.reservation;

import java.time.LocalDateTime;

public record SeatReservation(
    long id,
    long userId,
    long seatId,
    ReservationStatus status,
    LocalDateTime createdAt
) {
}

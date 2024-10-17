package ms.parade.infrastructure.reservation;

import java.time.LocalDateTime;

import ms.parade.domain.reservation.ReservationStatus;

public record SeatReservationParams(
    long userId,
    long seatId,
    ReservationStatus status,
    LocalDateTime createdAt
) {
}

package ms.parade.application.reservation;

import ms.parade.domain.reservation.SeatReservation;
import ms.parade.domain.seat.Seat;

public record SeatReservationResult(
    Seat seatInfo,
    SeatReservation seatReservation
) {
}

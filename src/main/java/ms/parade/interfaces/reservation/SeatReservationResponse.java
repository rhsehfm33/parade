package ms.parade.interfaces.reservation;

import ms.parade.application.reservation.SeatReservationResult;

public record SeatReservationResponse(
    SeatReservationResult seatReservationResult
) {
}

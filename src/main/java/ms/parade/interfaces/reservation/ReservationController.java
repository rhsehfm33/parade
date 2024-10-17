package ms.parade.interfaces.reservation;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import ms.parade.application.reservation.ReservationFacade;
import ms.parade.application.reservation.SeatReservationResult;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1")
public class ReservationController {
    private final ReservationFacade reservationFacade;

    @PostMapping("/protected/seat-reservations")
    ResponseEntity<SeatReservationResponse> reserveSeat(
        Authentication authentication,
        @RequestBody SeatReservationRequest seatReservationRequest
    ) {
        SeatReservationResult seatReservationResult = reservationFacade.reserveSeat(
            (long)authentication.getPrincipal(),
            seatReservationRequest.seatId()
        );

        return ResponseEntity.ok(new SeatReservationResponse(seatReservationResult));
    }
}

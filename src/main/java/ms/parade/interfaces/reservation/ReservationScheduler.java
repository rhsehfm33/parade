package ms.parade.interfaces.reservation;

import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import ms.parade.domain.concert.ConcertService;
import ms.parade.domain.reservation.ReservationStatus;
import ms.parade.domain.reservation.SeatReservation;
import ms.parade.domain.reservation.SeatReservationService;
import ms.parade.domain.seat.Seat;
import ms.parade.domain.seat.SeatService;
import ms.parade.domain.seat.SeatStatus;

@Component
@RequiredArgsConstructor
public class ReservationScheduler {
    private final SeatService seatService;
    private final ConcertService concertService;
    private final SeatReservationService seatReservationService;

    @Transactional
    @Scheduled(fixedRate = 60000)
    public void cancelTimeoutReservation() {
        List<SeatReservation> seatReservations = seatReservationService.findTimeoutReservationsForUpdate();
        List<Long> seatIds = seatReservations.stream().map(SeatReservation::seatId).toList();
        List<Seat> seats = seatService.findByIdsForUpdate(seatIds);
        for (SeatReservation seatReservation : seatReservations) {
            seatReservationService.updateStatus(seatReservation.id(), ReservationStatus.CANCEL);
        }
        for (Seat seat : seats) {
            seatService.updateStatus(seat.id(), SeatStatus.EMPTY);
            concertService.addAvailableSeats(seat.scheduleId(), 1);
        }
    }
}

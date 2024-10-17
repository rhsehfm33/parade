package ms.parade.domain.reservation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import ms.parade.infrastructure.reservation.SeatReservationParams;

public interface SeatReservationRepository {
    SeatReservation save(SeatReservationParams seatReservationParams);
    SeatReservation updateStatus(long seatId, ReservationStatus status);
    Optional<SeatReservation> findByIdForUpdate(long id);
    List<SeatReservation> findByStatusAndCreatedAtBeforeForUpdate(ReservationStatus status, LocalDateTime limit);
}

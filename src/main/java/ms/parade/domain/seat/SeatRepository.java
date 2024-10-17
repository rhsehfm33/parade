package ms.parade.domain.seat;

import java.util.List;
import java.util.Optional;

import ms.parade.infrastructure.seat.SeatParams;

public interface SeatRepository {
    List<Seat> findByScheduleIdAndSeatStatus(long scheduleId, SeatStatus seatStatus);
    Optional<Seat> findByIdForUpdate(long id);
    List<Seat> findByIdsForUpdate(List<Long> id);
    Seat updateStatus(long id, SeatStatus seatStatus);
    Seat save(SeatParams seatParams);
}

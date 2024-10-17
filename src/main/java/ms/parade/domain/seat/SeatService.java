package ms.parade.domain.seat;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class SeatService {
    private final SeatRepository seatRepository;

    public List<Seat> findAvailableSeats(long scheduleId) {
        return seatRepository.findByScheduleIdAndSeatStatus(scheduleId, SeatStatus.EMPTY);
    }

    public Optional<Seat> findByIdForUpdate(long seatId) {
        return seatRepository.findByIdForUpdate(seatId);
    }

    public List<Seat> findByIdsForUpdate(List<Long> seatIds) {
        return seatRepository.findByIdsForUpdate(seatIds);
    }

    public Seat updateStatus(long seatId, SeatStatus status) {
        return seatRepository.updateStatus(seatId, status);
    }
}

package ms.parade.domain.concert;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ConcertService {
    private final ConcertRepository concertRepository;

    public List<ConcertScheduleInfo> findConcertSchedulesByConcertId(long concertId) {
        List<ConcertSchedule> concertSchedules = concertRepository.findSchedulesByConcertId(concertId);
        return concertSchedules.stream().map(ConcertScheduleInfo::new).toList();
    }

    public ConcertSchedule addAvailableSeats(long scheduleId, int amount) {
        ConcertSchedule concertSchedule = concertRepository.findScheduleByIdForUpdate(scheduleId).orElseThrow(
            () -> new EntityNotFoundException("존재하지 않는 콘서트 스케쥴입니다.")
        );
        int nextAvailableSeats = concertSchedule.availableSeats() + amount;
        if (nextAvailableSeats < 0 || nextAvailableSeats > concertSchedule.allSeats()) {
            throw new IllegalArgumentException("잔여석은 0이상 최대 좌석 수 이하여야 합니다.");
        }
        return concertRepository.updateScheduleAvailableSeats(scheduleId, nextAvailableSeats);
    }
}

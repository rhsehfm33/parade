package ms.parade.domain.concert;

import java.util.List;
import java.util.Optional;

public interface ConcertRepository {
    List<ConcertSchedule> findSchedulesByConcertId(long concertId);
    Optional<ConcertSchedule> findScheduleByIdForUpdate(long id);
    ConcertSchedule updateScheduleAvailableSeats(long id, int availableSeats);
}

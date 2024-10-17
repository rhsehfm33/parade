package ms.parade.domain.concert;

import java.time.LocalDate;

public record ConcertScheduleInfo(
    long id,
    long concertId,
    int allSeats,
    int availableSeats,
    LocalDate performanceDate
) {
    public ConcertScheduleInfo(ConcertSchedule concertSchedule) {
        this(
            concertSchedule.id(),
            concertSchedule.concertId(),
            concertSchedule.allSeats(),
            concertSchedule.availableSeats(),
            concertSchedule.performanceDate()
        );
    }
}

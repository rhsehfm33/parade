package ms.parade.interfaces.concert;

import java.time.LocalDate;

import ms.parade.domain.concert.ConcertScheduleInfo;

public record ConcertResponse(
    long id,
    long concertId,
    int allSeats,
    int availableSeats,
    LocalDate performanceDate
) {
    public ConcertResponse(ConcertScheduleInfo concertScheduleInfo) {
        this(
            concertScheduleInfo.id(),
            concertScheduleInfo.concertId(),
            concertScheduleInfo.allSeats(),
            concertScheduleInfo.availableSeats(),
            concertScheduleInfo.performanceDate()
        );
    }
}

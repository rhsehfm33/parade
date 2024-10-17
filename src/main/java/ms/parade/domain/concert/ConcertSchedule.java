package ms.parade.domain.concert;

import java.time.LocalDate;

public record ConcertSchedule(
    long id,
    long concertId,
    int allSeats,
    int availableSeats,
    LocalDate performanceDate
) {
}

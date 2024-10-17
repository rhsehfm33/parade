package ms.parade.infrastructure.concert;

import java.time.LocalDate;

public record ConcertScheduleParams(
    long concertId,
    int allSeats,
    int availableSeats,
    LocalDate performanceDate
) {
}

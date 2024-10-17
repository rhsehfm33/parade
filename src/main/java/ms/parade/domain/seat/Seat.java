package ms.parade.domain.seat;

public record Seat(
    long id,
    long scheduleId,
    String name,
    long price,
    SeatStatus status
) {
}

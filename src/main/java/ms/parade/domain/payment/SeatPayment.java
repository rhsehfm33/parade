package ms.parade.domain.payment;

public record SeatPayment(
    long id,
    long userId,
    long seatReservationId,
    String content
) {
}

package ms.parade.infrastructure.payment;

public record SeatPaymentParams(
    long userId,
    long seatReservationId,
    String content
) {
}

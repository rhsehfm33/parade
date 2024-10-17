package ms.parade.domain.payment;

import ms.parade.infrastructure.payment.SeatPaymentParams;

public record SeatPaymentCommand(SeatPaymentParams seatPaymentParams) {
}
